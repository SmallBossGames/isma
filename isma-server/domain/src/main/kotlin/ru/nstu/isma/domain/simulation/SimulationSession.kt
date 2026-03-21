package ru.nstu.isma.domain.simulation

data class SimulationSession(
    val startTime: Double,
    val endTime: Double,
    val currentTime: Double = 0.0,
    val status: SimulationStatus = SimulationStatus.RUNNING,
)
