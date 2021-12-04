package ru.nstu.isma.next.core.sim.controller.services.controllers

import ru.nstu.isma.next.core.sim.controller.models.HybridSystemIntegrationResult
import ru.nstu.isma.next.core.sim.controller.models.IntegratorApiParameters

interface ISimulationCoreController {
    /**
     * Моделирование
     */
    suspend fun simulateAsync(parameters: IntegratorApiParameters): HybridSystemIntegrationResult
}