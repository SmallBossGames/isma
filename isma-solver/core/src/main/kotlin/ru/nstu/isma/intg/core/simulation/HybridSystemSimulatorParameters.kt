package ru.nstu.isma.intg.core.simulation

import ru.nstu.isma.intg.api.models.IntgResultPoint

data class HybridSystemSimulatorParameters(
    val hsmCompilationResult: HsmCompilationResult,
    val simulationInitials: SimulationInitials,
    val resultPointHandlers: (point: IntgResultPoint) -> Unit,
    val stepChangeHandlers: (value: Double) -> Unit,
)
