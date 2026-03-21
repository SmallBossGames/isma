package ru.nstu.isma.domain.simulation

import ru.nstu.isma.next.core.sim.controller.models.HybridSystemIntegrationResult

data class SimulationSession(
    val simulationId: Long,
    val startTime: Double,
    val endTime: Double,
    val currentTime: Double = 0.0,
    val status: SimulationStatus = SimulationStatus.RUNNING,
    val resultFilePath: String? = null,
    val error: String? = null,
)
