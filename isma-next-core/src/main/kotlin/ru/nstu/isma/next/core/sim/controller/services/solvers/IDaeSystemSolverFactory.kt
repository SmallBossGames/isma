package ru.nstu.isma.next.core.sim.controller.services.solvers

import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver
import ru.nstu.isma.next.core.sim.controller.models.HsmCompilationResult

interface IDaeSystemSolverFactory {
    fun create(hsmCompilationResult: HsmCompilationResult): DaeSystemStepSolver
}