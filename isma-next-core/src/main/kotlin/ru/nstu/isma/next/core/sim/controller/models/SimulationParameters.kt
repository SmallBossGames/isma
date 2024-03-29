package ru.nstu.isma.next.core.sim.controller.models

data class SimulationParameters(
    val compilationResult: HsmCompilationResult,
    val simulationInitials: SimulationInitials,
    val stepChangeHandlers: (value: Double) -> Unit,
)