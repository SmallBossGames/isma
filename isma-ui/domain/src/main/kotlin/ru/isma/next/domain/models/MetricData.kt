package ru.isma.next.domain.models

data class MetricData(
    val startTime: Long = 0L,
    val endTime: Long = 0L,
) {
    val simulationTime: Long get() = endTime - startTime
}
