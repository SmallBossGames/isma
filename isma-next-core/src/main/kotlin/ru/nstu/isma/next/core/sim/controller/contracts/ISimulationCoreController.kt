package ru.nstu.isma.next.core.sim.controller.contracts

import ru.nstu.isma.next.core.sim.controller.HybridSystemIntegrationResult
import ru.nstu.isma.next.core.sim.controller.models.IntegratorApiParameters

interface ISimulationCoreController {
    /**
     * Моделирование
     */
    suspend fun simulateAsync(parameters: IntegratorApiParameters): HybridSystemIntegrationResult
}