package ru.nstu.isma.intg.core.simulation

import ru.nstu.isma.intg.api.models.IntgMetricData

interface IHybridSystemSimulator {
    fun runAsync(parameters: HybridSystemSimulatorParameters): IntgMetricData
}
