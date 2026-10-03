package ru.nstu.isma.intg.core.simulation

import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver

interface IDaeSystemSolverFactory {
    fun create(hsmCompilationResult: HsmCompilationResult): DaeSystemStepSolver
}
