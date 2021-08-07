package ru.nstu.isma.next.core.sim.controller.models

import ru.nstu.isma.intg.api.calcmodel.HybridSystem
import ru.nstu.isma.intg.api.models.IntgResultPoint
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver
import ru.nstu.isma.intg.core.methods.EventDetectionIntgController
import ru.nstu.isma.next.core.sim.controller.SimulationInitials

data class HybridSystemSimulatorParameters(
    val hybridSystem: HybridSystem,
    val stepSolver: DaeSystemStepSolver,
    val simulationInitials: SimulationInitials,
    val eventDetector: EventDetectionIntgController,
    val eventDetectionStepBoundLow: Double,
    val resultPointHandlers: List<suspend (point: IntgResultPoint) -> Unit>,
    val stepChangeHandlers: List<suspend (value: Double) -> Unit>,
)