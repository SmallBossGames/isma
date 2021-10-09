package ru.nstu.isma.next.core.sim.controller

import kotlinx.coroutines.coroutineScope
import ru.nstu.isma.core.hsm.HSM
import ru.nstu.isma.intg.core.methods.EventDetectionIntgController
import ru.nstu.isma.next.core.sim.controller.contracts.IHsmCompiler
import ru.nstu.isma.next.core.sim.controller.contracts.ISimulationCoreController
import ru.nstu.isma.next.core.sim.controller.gen.EquationIndexProvider
import ru.nstu.isma.next.core.sim.controller.models.IntegratorApiParameters
import ru.nstu.isma.next.core.sim.controller.models.SimulationParameters
import ru.nstu.isma.next.core.sim.controller.services.ISimulationRunnerFactory

/**
 * Created by Bessonov Alex
 * on 04.01.2015.
 */
class SimulationCoreController(
    private val simulationRunnerProvider: ISimulationRunnerFactory,
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

        val context = SimulationParameters(
            compilationResult,
            initials,
            parameters.stepChangeHandlers
        )

        return@coroutineScope simulationRunnerProvider.create().run(context)
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