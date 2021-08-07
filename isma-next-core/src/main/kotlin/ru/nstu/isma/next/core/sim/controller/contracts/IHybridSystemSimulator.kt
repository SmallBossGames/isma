package ru.nstu.isma.next.core.sim.controller.contracts

import ru.nstu.isma.intg.api.IntgMetricData
import ru.nstu.isma.next.core.sim.controller.models.HybridSystemSimulatorParameters

interface IHybridSystemSimulator {
    suspend fun runAsync(parameters: HybridSystemSimulatorParameters): IntgMetricData
}