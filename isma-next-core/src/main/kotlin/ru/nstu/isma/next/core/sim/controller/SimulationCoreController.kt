package ru.nstu.isma.next.core.sim.controller

import kotlinx.coroutines.coroutineScope
import ru.nstu.isma.core.hsm.HSM
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver
import ru.nstu.isma.intg.api.solvers.useAsync
import ru.nstu.isma.intg.core.methods.EventDetectionIntgController
import ru.nstu.isma.intg.core.solvers.DefaultDaeSystemStepSolver
import ru.nstu.isma.intg.server.client.RemoteDaeSystemStepSolver
import ru.nstu.isma.next.core.sim.controller.contracts.IHsmCompiler
import ru.nstu.isma.next.core.sim.controller.contracts.ISimulationCoreController
import ru.nstu.isma.next.core.sim.controller.gen.EquationIndexProvider
import ru.nstu.isma.next.core.sim.controller.models.IntegratorApiParameters
import ru.nstu.isma.next.core.sim.controller.models.SimulationParameters
import ru.nstu.isma.next.core.sim.controller.services.ComputeEngineClientFactory
import ru.nstu.isma.next.core.sim.controller.services.IIntegrationMethodProvider
import ru.nstu.isma.next.core.sim.controller.services.ISimulationRunnerProvider

/**
 * Created by Bessonov Alex
 * on 04.01.2015.
 */
class SimulationCoreController(
    private val simulationRunnerProvider: ISimulationRunnerProvider,
    private val integrationMethodProvider: IIntegrationMethodProvider,
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

        val method = integrationMethodProvider.method

        val stepSolver: DaeSystemStepSolver = if (parameters.parallelParameters != null) {
            val computeEngineClient = ComputeEngineClientFactory().create(
                parameters.parallelParameters.server,
                parameters.parallelParameters.port,
                method,
                compilationResult
            )
            RemoteDaeSystemStepSolver(method, computeEngineClient)
        } else {
            DefaultDaeSystemStepSolver(method, compilationResult.hybridSystem.daeSystem)
        }

        return@coroutineScope stepSolver.useAsync {
            val eventDetector = if (parameters.eventDetectionParameters != null)
                EventDetectionIntgController(parameters.eventDetectionParameters.gamma, true)
            else
                EventDetectionIntgController(0.0, false)

            val stepBoundLow = parameters.eventDetectionParameters?.stepBoundLow ?: 0.0

            val context = SimulationParameters(
                compilationResult.hybridSystem,
                initials,
                compilationResult.indexProvider,
                stepSolver,
                eventDetector,
                stepBoundLow,
                parameters.stepChangeHandlers
            )

            return@useAsync simulationRunnerProvider.runner.run(context)
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