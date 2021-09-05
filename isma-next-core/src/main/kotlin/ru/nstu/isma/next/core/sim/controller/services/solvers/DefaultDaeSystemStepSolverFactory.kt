package ru.nstu.isma.next.core.sim.controller.services.solvers

import ru.nstu.isma.intg.api.calcmodel.DaeSystem
import ru.nstu.isma.intg.api.methods.IntgMethod
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver
import ru.nstu.isma.intg.core.solvers.DefaultDaeSystemStepSolver

class DefaultDaeSystemStepSolverFactory {
    fun create(method: IntgMethod, daeSystem: DaeSystem): DefaultDaeSystemStepSolver {
        return DefaultDaeSystemStepSolver(method, daeSystem)
    }
}