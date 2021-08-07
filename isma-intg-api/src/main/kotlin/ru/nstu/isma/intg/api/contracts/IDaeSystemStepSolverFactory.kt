package ru.nstu.isma.intg.api.contracts

import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver

interface IDaeSystemStepSolverFactory {
    fun createSolver(): DaeSystemStepSolver
}