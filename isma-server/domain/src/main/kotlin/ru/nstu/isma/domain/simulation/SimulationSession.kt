package ru.nstu.isma.domain.simulation

data class SimulationSession(
    val simulationId: Long = 0L,
    val startTime: Double,
    val endTime: Double,
    val currentTime: Double = 0.0,
    val status: SimulationStatus = SimulationStatus.RUNNING,
    val resultFilePath: String? = null,
    val error: String? = null,
)
