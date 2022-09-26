package ru.nstu.isma.next.core.sim.controller.services.runners

import kotlinx.coroutines.coroutineScope
import ru.nstu.isma.intg.api.IntgMetricData
import ru.nstu.isma.intg.api.providers.MemoryPointProvider
import ru.nstu.isma.next.core.sim.controller.models.HybridSystemIntegrationResult
import ru.nstu.isma.next.core.sim.controller.models.HybridSystemSimulatorParameters
import ru.nstu.isma.next.core.sim.controller.models.SimulationParameters
import ru.nstu.isma.next.core.sim.controller.services.simulators.IHybridSystemSimulator

class InMemorySimulationRunner(
    private val hybridSystemSimulator: IHybridSystemSimulator
) : ISimulationRunner {
    override suspend fun run(context: SimulationParameters): HybridSystemIntegrationResult = coroutineScope {
        val resultMemoryStore = MemoryPointProvider()

        val simulatorParameters = HybridSystemSimulatorParameters(
            context.compilationResult,
            context.simulationInitials,
            stepChangeHandlers = context.stepChangeHandlers,
            resultPointHandlers = resultMemoryStore::accept
        )

        val metricData: IntgMetricData = hybridSystemSimulator.runAsync(simulatorParameters)

        return@coroutineScope HybridSystemIntegrationResult(
            context.compilationResult.indexProvider,
            metricData,
            resultMemoryStore
        )
    }
}