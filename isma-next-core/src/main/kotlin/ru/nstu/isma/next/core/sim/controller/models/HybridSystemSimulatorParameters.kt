package ru.nstu.isma.next.core.sim.controller.models

import ru.nstu.isma.intg.api.models.IntgResultPoint

data class HybridSystemSimulatorParameters(
    val hsmCompilationResult: HsmCompilationResult,
    val simulationInitials: SimulationInitials,
    val resultPointHandlers: suspend (point: IntgResultPoint) -> Unit,
    val stepChangeHandlers: suspend (value: Double) -> Unit,
)