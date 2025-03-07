package ru.nstu.isma.next.core.sim.controller.services.controllers

import ru.nstu.isma.compiler.hsm.jvm.EquationIndexProvider
import ru.nstu.isma.core.hsm.HSM
import ru.nstu.isma.next.core.sim.controller.models.HybridSystemIntegrationResult
import ru.nstu.isma.next.core.sim.controller.models.IntegratorApiParameters
import ru.nstu.isma.next.core.sim.controller.models.SimulationInitials
import ru.nstu.isma.next.core.sim.controller.models.SimulationParameters
import ru.nstu.isma.next.core.sim.controller.services.hsm.IHsmCompiler
import ru.nstu.isma.next.core.sim.controller.services.runners.ISimulationRunner

/**
 * Created by Bessonov Alex
 * on 04.01.2015.
 */
class SimulationCoreController(
    private val simulationRunnerProvider: ISimulationRunner,
    private val hsmCompiler: IHsmCompiler,
) : ISimulationCoreController {
    /**
     * Моделирование
     */
    override suspend fun simulateAsync(parameters: IntegratorApiParameters): HybridSystemIntegrationResult {
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

        return simulationRunnerProvider.run(context)
    }

    private fun createOdeInitials(indexProvider: EquationIndexProvider, hsm: HSM): DoubleArray {
        val odeInitials = DoubleArray(hsm.variableTable.odes.size)

        for (ode in hsm.variableTable.odes) {
            val idx = indexProvider.getDifferentialEquationIndex(ode.code)!!
            odeInitials[idx] = ode.initialValue
        }

        return odeInitials
    }
}