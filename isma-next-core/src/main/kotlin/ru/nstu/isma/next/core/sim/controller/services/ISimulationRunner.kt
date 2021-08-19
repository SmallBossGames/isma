package ru.nstu.isma.next.core.sim.controller.services

import ru.nstu.isma.next.core.sim.controller.HybridSystemIntegrationResult
import ru.nstu.isma.next.core.sim.controller.models.InMemorySimulationParameters

interface ISimulationRunner {
    suspend fun run(context: InMemorySimulationParameters): HybridSystemIntegrationResult
}

