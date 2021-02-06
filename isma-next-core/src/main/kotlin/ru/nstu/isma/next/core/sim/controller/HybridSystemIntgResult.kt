package ru.nstu.isma.next.core.sim.controller

import ru.nstu.isma.intg.api.IntgMetricData
import ru.nstu.isma.intg.api.IntgResultPointProvider
import ru.nstu.isma.next.core.sim.controller.gen.EquationIndexProvider

/**
 * @author Maria Nasyrova
 * @since 06.10.2015
 */
class HybridSystemIntgResult(
        var equationIndexProvider: EquationIndexProvider? = null,
        var metricData: IntgMetricData? = null,
        var resultPointProvider: IntgResultPointProvider? = null)