package ru.nstu.isma.intg.api.models

/**
 * @author Maria
 * @since 03.04.2016
 */
class IntgMetricData {
    var startTime = 0L
    var endTime = 0L

    val simulationTime get() = endTime - startTime
}