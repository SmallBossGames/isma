package ru.nstu.isma.next.core.sim.controller.models

import ru.nstu.isma.intg.api.IntgMetricData
import ru.nstu.isma.intg.api.providers.IntegrationResultPointProvider
import ru.nstu.isma.next.core.simulation.gen.EquationIndexProvider

/**
 * @author Maria Nasyrova
 * @since 06.10.2015
 */
class HybridSystemIntegrationResult(
        var equationIndexProvider: EquationIndexProvider,
        var metricData: IntgMetricData,
        var resultPointProvider: IntegrationResultPointProvider
)