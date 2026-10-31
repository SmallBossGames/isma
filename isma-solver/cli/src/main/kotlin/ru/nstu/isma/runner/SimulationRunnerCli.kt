package ru.nstu.isma.runner

import org.slf4j.LoggerFactory
import ru.isma.next.exchange.format.writeAll
import ru.nstu.isma.compiler.hsm.core.HSM
import ru.nstu.isma.compiler.hsm.jvm.EquationIndexProvider
import ru.nstu.isma.lisma.runtime.LismaModelRuntime
import ru.nstu.isma.intg.api.methods.IIntegrationMethod
import ru.nstu.isma.intg.api.models.IntgResultPoint
import ru.nstu.isma.intg.api.providers.IIntegrationMethodProvider
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver
import ru.nstu.isma.intg.core.solvers.DefaultDaeSystemStepSolver
import ru.nstu.isma.intg.core.simulation.DefaultEventDetector
import ru.nstu.isma.intg.core.simulation.HsmCompilationResult
import ru.nstu.isma.intg.core.simulation.HybridSystemSimulator
import ru.nstu.isma.intg.core.simulation.HybridSystemSimulatorParameters
import ru.nstu.isma.intg.core.simulation.IDaeSystemSolverFactory
import ru.nstu.isma.intg.core.simulation.IEventDetector
import ru.nstu.isma.intg.core.simulation.IEventDetectorFactory
import ru.nstu.isma.intg.core.simulation.SimulationInitials
import ru.nstu.isma.next.integration.services.IntegrationMethodLibraryLoader
import ru.nstu.isma.next.integration.services.IntegrationMethodsLibrary
import java.io.File
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.system.exitProcess

/**
 * Standalone simulation runner CLI.
 *
 * Accepts a compiled-model `.class` file (produced by the LISMA compiler CLI,
 * with the HSM inlined as a blob) and simulation parameters, runs the numerical simulation, and writes the result
 * to a file in the ISMA exchange binary format. Progress is reported to a
 * progress file (one current-time value per step) so an orchestrator can poll
 * it. The process stops on SIGTERM/SIGINT (cancellation).
 *
 * Arguments:
 *   --list-methods      print the bundled integration method names (one per line) and exit
 *   --model <path>        path to the compiled-model .class file (required)
 *   --out <path>          path to write the result file (required)
 *   --progress <path>     path to write progress (current time) (optional)
 *   --start <double>      simulation start time (required)
 *   --end <double>        simulation end time (required)
 *   --step <double>       initial integration step (required)
 *   --method <name>       integration method name (required)
 *   --accuracy <double>   target accuracy (used when --accuracy-enabled)
 *   --accuracy-enabled    enable adaptive accuracy control
 *   --stability-enabled   enable stability control
 *   --gamma <double>      event-detection gamma (enables event detection with low-border)
 *   --low-border <double> event-detection step lower bound
 */
fun main(args: Array<String>) {
    // Batteries-included introspection: print the integration methods bundled with
    // this runner (one per line) and exit. Used by the daemon's ListSimulationMethods.
    if (args.contains("--list-methods")) {
        IntegrationMethodLibraryLoader.load().getIntegrationMethodNames().forEach { println(it) }
        return
    }

    val p = RunnerParams.parse(args)

    val logger = LoggerFactory.getLogger("simulation-runner")
    logger.info(
        "Running simulation: model={} method={} start={} end={} step={}",
        p.model, p.method, p.start, p.end, p.step,
    )

    // Cancellation: a shutdown hook (triggered by SIGTERM/SIGINT) flips the flag;
    // the integration loop checks it on every step.
    val cancelled = AtomicBoolean(false)
    Runtime.getRuntime().addShutdownHook(Thread { cancelled.set(true) })

    try {
        val classBytes = File(p.model).readBytes()
        val result = runSimulation(
            classBytes = classBytes,
            params = p,
            cancelled = cancelled,
        )
        if (result != null) {
            System.err.println("simulation-runner: $result")
            File(p.out).delete()
            exitProcess(1)
        }
        exitProcess(0)
    } catch (e: Throwable) {
        System.err.println("simulation-runner: ${e.message ?: e::class.java.name}")
        File(p.out).let { if (it.exists()) it.delete() }
        exitProcess(1)
    }
}

/**
 * Runs the simulation and writes the result file. Returns an error message on
 * failure, or null on success.
 */
private fun runSimulation(
    classBytes: ByteArray,
    params: RunnerParams,
    cancelled: AtomicBoolean,
): String? {
    val model = LismaModelRuntime.load(classBytes)
    model.hsm.initTimeEquation(params.start)

    val integrationMethodsLibrary: IntegrationMethodsLibrary = IntegrationMethodLibraryLoader.load()
    val method: IIntegrationMethod = integrationMethodsLibrary
        .getIntegrationMethod(params.method)
        .create()

    val accuracyController = method.accuracyController
    if (accuracyController != null) {
        accuracyController.enabled = params.accuracyEnabled
        if (params.accuracyEnabled) {
            accuracyController.accuracy = params.accuracy
        }
    }
    val stabilityController = method.stabilityController
    if (stabilityController != null) {
        stabilityController.enabled = params.stabilityEnabled
    }

    val compilationResult = HsmCompilationResult(model.equationIndexProvider, model.hybridSystem)

    val simulationInitials = SimulationInitials(
        differentialEquationInitials = createOdeInitials(model.equationIndexProvider, model.hsm),
        start = params.start,
        end = params.end,
        step = params.step,
    )
    val columnNames = buildColumnNames(model.equationIndexProvider)

    val outFile = File(params.out)
    val pointQueue = LinkedBlockingQueue<QueueItem>()

    val progressFile = params.progress?.let { File(it) }
    val writerThread = Thread.ofVirtual().start {
        writeAll(outFile, columnNames, pointsSequence(pointQueue))
    }

    var error: Throwable? = null
    try {
        run(
            compilationResult = compilationResult,
            method = method,
            simulationInitials = simulationInitials,
            gamma = params.gamma,
            lowBorder = params.lowBorder,
            onProgress = { currentTime ->
                progressFile?.let { writeProgress(it, currentTime) }
            },
            isCancelled = { cancelled.get() },
            onPoint = { point -> pointQueue.put(QueueItem.Point(point)) },
        )
    } catch (e: InterruptedException) {
        if (!cancelled.get()) {
            error = e
        }
    } catch (e: Throwable) {
        error = e
    } finally {
        pointQueue.put(QueueItem.EndOfStream)
    }
    writerThread.join()

    return error?.let { it.message ?: it::class.java.name }
}

private fun run(
    compilationResult: HsmCompilationResult,
    method: IIntegrationMethod,
    simulationInitials: SimulationInitials,
    gamma: Double?,
    lowBorder: Double?,
    onProgress: (currentTime: Double) -> Unit,
    isCancelled: () -> Boolean,
    onPoint: (point: DoubleArray) -> Unit,
) {
    val integrationMethodProvider = object : IIntegrationMethodProvider {
        override val method = method
    }

    val daeSystemSolverFactory = object : IDaeSystemSolverFactory {
        override fun create(hsmCompilationResult: HsmCompilationResult): DaeSystemStepSolver {
            return DefaultDaeSystemStepSolver(
                integrationMethodProvider.method,
                hsmCompilationResult.hybridSystem.daeSystem
            )
        }
    }

    val eventDetectorFactory = object : IEventDetectorFactory {
        override fun create(): IEventDetector? {
            return if (gamma != null && lowBorder != null) {
                DefaultEventDetector(gamma = gamma, stepLowBound = lowBorder)
            } else {
                null
            }
        }
    }

    val simulator = HybridSystemSimulator(daeSystemSolverFactory, eventDetectorFactory)

    simulator.runAsync(
        HybridSystemSimulatorParameters(
            compilationResult,
            simulationInitials,
            stepChangeHandlers = { currentTime ->
                if (isCancelled()) throw InterruptedException("Simulation was cancelled")
                onProgress(currentTime)
            },
            resultPointHandlers = { point -> onPoint(convertToDoubleArray(point)) },
        )
    )
}

private fun buildColumnNames(indexProvider: EquationIndexProvider): List<String> {
    val deCount = indexProvider.getDifferentialEquationCount()
    val aeCount = indexProvider.getAlgebraicEquationCount()

    val variableNames = mutableListOf<String>()
    variableNames.add("TIME")
    for (i in 0 until deCount) {
        val code = indexProvider.getDifferentialEquationCode(i)
        variableNames.add(if (code != null) "DE_$i-$code" else "DE_$i")
    }
    for (i in 0 until aeCount) {
        val code = indexProvider.getAlgebraicEquationCode(i)
        variableNames.add(if (code != null) "AE_$i-$code" else "AE_$i")
    }
    for (i in 0 until deCount) {
        variableNames.add("f$i")
    }
    return variableNames
}

private fun createOdeInitials(indexProvider: EquationIndexProvider, hsm: HSM): DoubleArray {
    val odeInitials = DoubleArray(hsm.variableTable.odes.size)
    for (ode in hsm.variableTable.odes) {
        val idx = indexProvider.getDifferentialEquationIndex(ode.code)!!
        odeInitials[idx] = ode.initialValue
    }
    return odeInitials
}

private fun convertToDoubleArray(point: IntgResultPoint): DoubleArray {
    val deCount = point.yForDe.size
    val aeCount = point.rhs[1].size
    val fCount = point.rhs[0].size

    val result = DoubleArray(1 + deCount + aeCount + fCount)
    var idx = 0
    result[idx++] = point.x
    for (v in point.yForDe) {
        result[idx++] = v
    }
    for (v in point.rhs[1]) {
        result[idx++] = v
    }
    for (v in point.rhs[0]) {
        result[idx++] = v
    }
    return result
}

private fun writeProgress(file: File, currentTime: Double) {
    file.writeText(currentTime.toString() + "\n")
}

private fun pointsSequence(queue: LinkedBlockingQueue<QueueItem>): Sequence<DoubleArray> = sequence {
    while (true) {
        val item = queue.take()
        if (item is QueueItem.EndOfStream) break
        yield((item as QueueItem.Point).point)
    }
}

private sealed class QueueItem {
    data class Point(val point: DoubleArray) : QueueItem()
    data object EndOfStream : QueueItem()
}
