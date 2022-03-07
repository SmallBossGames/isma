package ru.nstu.isma.next.core.sim.controller.services.runners

import ru.nstu.isma.next.core.sim.controller.models.HybridSystemIntegrationResult
import ru.nstu.isma.next.core.sim.controller.models.SimulationParameters

interface ISimulationRunner {
    suspend fun run(context: SimulationParameters): HybridSystemIntegrationResult
}

