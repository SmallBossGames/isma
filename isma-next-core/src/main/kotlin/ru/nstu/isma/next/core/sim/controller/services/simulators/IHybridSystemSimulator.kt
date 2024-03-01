package ru.nstu.isma.next.core.sim.controller.services.simulators

import ru.nstu.isma.intg.api.models.IntgMetricData
import ru.nstu.isma.next.core.sim.controller.models.HybridSystemSimulatorParameters

interface IHybridSystemSimulator {
    suspend fun runAsync(parameters: HybridSystemSimulatorParameters): IntgMetricData
}