package ru.nstu.isma.runner

import ru.nstu.isma.compiler.hsm.core.HSM
import ru.nstu.isma.compiler.hsm.jvm.EquationIndexProvider
import ru.nstu.isma.intg.api.methods.IIntegrationMethod
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
import ru.nstu.isma.lisma.runtime.CompiledLismaModel
import ru.nstu.isma.lisma.runtime.LismaModelRuntime
import ru.nstu.isma.next.integration.services.IntegrationMethodLibraryLoader
import java.io.File
import java.io.OutputStream

/**
 * Runs a numerical simulation of a compiled LISMA model and streams the result
 * to an [OutputStream] in the ISMA exchange binary format.
 *
 * [params] holds the simulation parameters; [isCancelled] is polled on every
 * integration step so a shutdown (SIGTERM/SIGINT) stops the run promptly.
 *
 * [run] returns an error message on failure, or null on success. A cancelled
 * run is not an error: it returns null.
 */
class SimulationRunner(
    private val params: RunnerParams,
    private val isCancelled: () -> Boolean,
) {
    fun run(modelFile: File, out: OutputStream): String? {
        return try {
            doRun(modelFile, out)
        } catch (e: InterruptedException) {
            if (isCancelled()) null else messageOf(e)
        } catch (e: Throwable) {
            messageOf(e)
        }
    }

    private fun doRun(modelFile: File, out: OutputStream): String? {
        val model = LismaModelRuntime.load(modelFile)
        model.hsm.initTimeEquation(params.start)

        val method = loadMethod()
        configureControllers(method)

        val compilationResult = HsmCompilationResult(model.equationIndexProvider, model.hybridSystem)
        val initials = buildInitials(model)
        val layout = ResultColumnLayout(model.equationIndexProvider)

        val writer = ResultWriter(out, layout.names)
        try {
            runSimulation(compilationResult, method, initials, layout, writer)
        } finally {
            writer.close()
        }
        return null
    }

    private fun loadMethod(): IIntegrationMethod =
        IntegrationMethodLibraryLoader.load().getIntegrationMethod(params.method).create()

    private fun configureControllers(method: IIntegrationMethod) {
        method.accuracyController?.let { controller ->
            controller.enabled = params.accuracyEnabled
            if (params.accuracyEnabled) {
                controller.accuracy = params.accuracy
            }
        }
        method.stabilityController?.enabled = params.stabilityEnabled
    }

    private fun buildInitials(model: CompiledLismaModel): SimulationInitials =
        SimulationInitials(
            differentialEquationInitials = createOdeInitials(model.equationIndexProvider, model.hsm),
            start = params.start,
            end = params.end,
            step = params.step,
        )

    private fun createOdeInitials(indexProvider: EquationIndexProvider, hsm: HSM): DoubleArray {
        val odeInitials = DoubleArray(hsm.variableTable.odes.size)
        for (ode in hsm.variableTable.odes) {
            val index = requireNotNull(indexProvider.getDifferentialEquationIndex(ode.code)) {
                "No index for differential equation ${ode.code}"
            }
            odeInitials[index] = ode.initialValue
        }
        return odeInitials
    }

    private fun runSimulation(
        compilationResult: HsmCompilationResult,
        method: IIntegrationMethod,
        initials: SimulationInitials,
        layout: ResultColumnLayout,
        writer: ResultWriter,
    ) {
        val simulator = HybridSystemSimulator(
            DaeSystemSolverFactory(method),
            EventDetectorFactory(params.gamma, params.lowBorder),
        )
        simulator.runAsync(
            HybridSystemSimulatorParameters(
                hsmCompilationResult = compilationResult,
                simulationInitials = initials,
                resultPointHandlers = { point -> writer.write(layout.flatten(point)) },
                stepChangeHandlers = { checkNotCancelled() },
            )
        )
    }

    private fun checkNotCancelled() {
        if (isCancelled()) {
            throw InterruptedException("Simulation was cancelled")
        }
    }
}

/** Builds the step solver for a fixed integration method. */
private class DaeSystemSolverFactory(
    private val method: IIntegrationMethod,
) : IDaeSystemSolverFactory {
    override fun create(hsmCompilationResult: HsmCompilationResult): DaeSystemStepSolver =
        DefaultDaeSystemStepSolver(method, hsmCompilationResult.hybridSystem.daeSystem)
}

/**
 * Builds the event detector. Event detection is active only when both a gamma
 * and a step lower bound are supplied.
 */
private class EventDetectorFactory(
    private val gamma: Double?,
    private val lowBorder: Double?,
) : IEventDetectorFactory {
    override fun create(): IEventDetector? =
        if (gamma != null && lowBorder != null) {
            DefaultEventDetector(gamma, lowBorder)
        } else {
            null
        }
}

private fun messageOf(error: Throwable): String = error.message ?: error::class.java.name
