package ru.nstu.isma.server.infrastructure.simulation

import ru.nstu.isma.compiler.hsm.core.HSM
import ru.nstu.isma.compiler.hsm.jvm.EquationIndexProvider
import ru.nstu.isma.domain.handlers.runSimulation.RunSimulationParameters
import ru.nstu.isma.domain.simulation.ISimulationExecutor
import ru.nstu.isma.domain.simulation.ISimulationSessionStore
import ru.nstu.isma.intg.api.models.IntgResultPoint
import ru.nstu.isma.intg.api.providers.IIntegrationMethodProvider
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver
import ru.nstu.isma.intg.api.utilities.IntegrationResultPointFileHelpers
import ru.nstu.isma.intg.core.solvers.DefaultDaeSystemStepSolver
import ru.nstu.isma.next.core.sim.controller.models.HybridSystemSimulatorParameters
import ru.nstu.isma.next.core.sim.controller.models.SimulationInitials
import ru.nstu.isma.next.core.sim.controller.services.eventDetection.IEventDetector
import ru.nstu.isma.next.core.sim.controller.services.hsm.IHsmCompiler
import ru.nstu.isma.next.core.sim.controller.services.simulators.HybridSystemSimulator
import ru.nstu.isma.next.core.sim.controller.services.solvers.IDaeSystemSolverFactory
import ru.nstu.isma.next.integration.services.IntegrationMethodsLibrary
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue

class SimulationExecutorImpl(
    private val integrationMethodsLibrary: IntegrationMethodsLibrary,
    private val hsmCompiler: IHsmCompiler,
    private val sessionStore: ISimulationSessionStore,
    private val executorService: ExecutorService,
) : ISimulationExecutor {

    override fun execute(simulationId: Long, parameters: RunSimulationParameters, hsm: HSM) {
        executorService.submit {
            try {
                runSimulation(simulationId, parameters, hsm)
            } catch (e: Exception) {
                sessionStore.failSimulation(simulationId, e.message ?: "Unknown error")
            }
        }
    }

    private fun runSimulation(simulationId: Long, parameters: RunSimulationParameters, hsm: HSM) {
        hsm.initTimeEquation(parameters.startTime)

        val integrationMethod = integrationMethodsLibrary
            .getIntegrationMethod(parameters.methodName)
            .create()

        val accuracyController = integrationMethod.accuracyController
        if (accuracyController != null) {
            accuracyController.enabled = parameters.isAccuracyInUse
            if (parameters.isAccuracyInUse) {
                accuracyController.accuracy = parameters.accuracy
            }
        }

        val integrationMethodProvider = object : IIntegrationMethodProvider {
            override val method = integrationMethod
        }

        val daeSystemSolverFactory = object : IDaeSystemSolverFactory {
            override fun create(hsmCompilationResult: ru.nstu.isma.next.core.sim.controller.models.HsmCompilationResult): DaeSystemStepSolver {
                return DefaultDaeSystemStepSolver(
                    integrationMethodProvider.method,
                    hsmCompilationResult.hybridSystem.daeSystem
                )
            }
        }

        val eventDetectorFactory = object : ru.nstu.isma.next.core.sim.controller.services.eventDetection.IEventDetectorFactory {
            override fun create(): IEventDetector? = null
        }

        val simulator = HybridSystemSimulator(daeSystemSolverFactory, eventDetectorFactory)

        val compilationResult = hsmCompiler.compile(hsm)

        val differentialEquationInitials = createOdeInitials(compilationResult.indexProvider, hsm)

        val simulationInitials = SimulationInitials(
            differentialEquationInitials = differentialEquationInitials,
            start = parameters.startTime,
            end = parameters.endTime,
            step = parameters.initialStep
        )

        val tempFile = File.createTempFile("isma_simulation_$simulationId", ".csv")

        var metricData: ru.nstu.isma.intg.api.models.IntgMetricData? = null
        val pointQueue = LinkedBlockingQueue<QueueItem>()

        val simulatorThread = Thread.ofVirtual().start {
            val simulatorParameters = HybridSystemSimulatorParameters(
                compilationResult,
                simulationInitials,
                stepChangeHandlers = { currentTime ->
                    sessionStore.updateProgress(simulationId, currentTime)
                },
                resultPointHandlers = { point ->
                    pointQueue.put(QueueItem.Point(point))
                }
            )

            metricData = simulator.runAsync(simulatorParameters)
            pointQueue.put(QueueItem.EndOfStream)
        }

        val writerThread = Thread.ofVirtual().start {
            tempFile.bufferedWriter().use { writer ->
                var isFirst = true
                while (true) {
                    val item = pointQueue.take()
                    if (item is QueueItem.EndOfStream) break
                    val point = (item as QueueItem.Point).point
                    if (isFirst) {
                        writer.append(IntegrationResultPointFileHelpers.buildCsvHeader(point))
                        isFirst = false
                    }
                    writer.append(IntegrationResultPointFileHelpers.buildCsvString(point))
                }
            }
        }

        simulatorThread.join()
        writerThread.join()

        sessionStore.completeSimulation(simulationId, tempFile.absolutePath)
    }

    private fun createOdeInitials(indexProvider: EquationIndexProvider, hsm: HSM): DoubleArray {
        val odeInitials = DoubleArray(hsm.variableTable.odes.size)

        for (ode in hsm.variableTable.odes) {
            val idx = indexProvider.getDifferentialEquationIndex(ode.code)!!
            odeInitials[idx] = ode.initialValue
        }

        return odeInitials
    }

    private sealed class QueueItem {
        data class Point(val point: IntgResultPoint) : QueueItem()
        data object EndOfStream : QueueItem()
    }
}
