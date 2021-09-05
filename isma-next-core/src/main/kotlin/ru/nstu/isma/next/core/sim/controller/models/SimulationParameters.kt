package ru.nstu.isma.next.core.sim.controller.models

import ru.nstu.isma.intg.api.calcmodel.HybridSystem
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver
import ru.nstu.isma.intg.core.methods.EventDetectionIntgController
import ru.nstu.isma.next.core.sim.controller.HybridSystemSimulator
import ru.nstu.isma.next.core.sim.controller.SimulationInitials
import ru.nstu.isma.next.core.sim.controller.gen.EquationIndexProvider

data class SimulationParameters(
    val compilationResult: HsmCompilationResult,
    val simulationInitials: SimulationInitials,
    val eventDetector: EventDetectionIntgController,
    val eventDetectionStepBoundLow: Double,
    val stepChangeHandlers: List<suspend (value: Double) -> Unit>,
)