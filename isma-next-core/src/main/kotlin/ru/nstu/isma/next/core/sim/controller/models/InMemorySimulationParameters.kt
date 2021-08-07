package ru.nstu.isma.next.core.sim.controller.models

import ru.nstu.isma.intg.api.calcmodel.HybridSystem
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver
import ru.nstu.isma.intg.core.methods.EventDetectionIntgController
import ru.nstu.isma.next.core.sim.controller.HybridSystemSimulator
import ru.nstu.isma.next.core.sim.controller.SimulationInitials
import ru.nstu.isma.next.core.sim.controller.gen.EquationIndexProvider

data class InMemorySimulationParameters(
    val hybridSystem: HybridSystem,
    val simulationInitials: SimulationInitials,
    val indexProvider: EquationIndexProvider,
    val cauchyProblemSolver: HybridSystemSimulator,
    val stepSolver: DaeSystemStepSolver,
    val eventDetector: EventDetectionIntgController,
    val eventDetectionStepBoundLow: Double
)