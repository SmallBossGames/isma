package ru.nstu.isma.next.core.sim.controller

import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import ru.nstu.isma.core.hsm.HSM
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver
import ru.nstu.isma.intg.core.methods.EventDetectionIntgController
import ru.nstu.isma.intg.core.solvers.DefaultDaeSystemStepSolver
import ru.nstu.isma.intg.server.client.ComputeEngineClient
import ru.nstu.isma.intg.server.client.RemoteDaeSystemStepSolver
import ru.nstu.isma.next.core.sim.controller.contracts.IHsmCompiler
import ru.nstu.isma.next.core.sim.controller.contracts.ISimulationCoreController
import ru.nstu.isma.next.core.sim.controller.gen.EquationIndexProvider
import ru.nstu.isma.next.core.sim.controller.models.*
import ru.nstu.isma.next.core.sim.controller.services.ISimulationRunnerProvider

/**
 * Created by Bessonov Alex
 * on 04.01.2015.
 */
class SimulationCoreController(
    private val simulationRunnerProvider: ISimulationRunnerProvider,
    private val hsmCompiler: IHsmCompiler,
) : ISimulationCoreController {
    /**
     * Моделирование
     */
    override suspend fun simulateAsync(parameters: IntegratorApiParameters): HybridSystemIntegrationResult = coroutineScope {
        val compilationResult = hsmCompiler.compile(parameters.hsm)

        val initials = SimulationInitials(
            differentialEquationInitials = createOdeInitials(compilationResult.indexProvider, parameters.hsm),
            start = parameters.initials.start,
            end = parameters.initials.end,
            step = parameters.initials.stepSize
        )

        var computeEngineClient: ComputeEngineClient? = null
        return@coroutineScope try {
            val stepSolver: DaeSystemStepSolver = if (parameters.parallelParameters != null) {
                computeEngineClient = ComputeEngineClient(compilationResult.classLoader).apply {
                    connect(parameters.parallelParameters.server, parameters.parallelParameters.port)
                    loadIntgMethod(parameters.method)
                    loadDaeSystem(compilationResult.hybridSystem.daeSystem)
                }
                RemoteDaeSystemStepSolver(parameters.method, computeEngineClient)
            } else {
                DefaultDaeSystemStepSolver(parameters.method, compilationResult.hybridSystem.daeSystem)
            }

            val eventDetector = if (parameters.eventDetectionParameters != null)
                EventDetectionIntgController(parameters.eventDetectionParameters.gamma, true)
            else
                EventDetectionIntgController(0.0, false)

            val stepBoundLow = parameters.eventDetectionParameters?.stepBoundLow ?: 0.0

            val context = InMemorySimulationParameters(
                compilationResult.hybridSystem,
                initials,
                compilationResult.indexProvider,
                stepSolver,
                eventDetector,
                stepBoundLow,
                parameters.stepChangeHandlers
            )

            simulationRunnerProvider.runner.run(context)

        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            e.printStackTrace()
            throw RuntimeException(e)
        } finally {
            computeEngineClient?.disconnect()
        }
    }

    private suspend fun createOdeInitials(indexProvider: EquationIndexProvider, hsm: HSM): DoubleArray = coroutineScope {
        val odeInitials = DoubleArray(hsm.variableTable.odes.size)

        for (ode in hsm.variableTable.odes) {
            val idx = indexProvider.getDifferentialEquationIndex(ode.code)!!
            odeInitials[idx] = ode.initialValue
        }

        return@coroutineScope odeInitials
    }
}