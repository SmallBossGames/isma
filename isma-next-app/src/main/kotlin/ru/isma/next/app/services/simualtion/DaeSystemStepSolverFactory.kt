package ru.isma.next.app.services.simualtion

import ru.nstu.isma.intg.api.providers.IIntegrationMethodProvider
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver
import ru.nstu.isma.next.core.sim.controller.models.HsmCompilationResult
import ru.nstu.isma.next.core.sim.controller.services.solvers.IDaeSystemSolverFactory
import ru.nstu.isma.next.intg.core.solvers.DefaultDaeSystemStepSolver

class DaeSystemStepSolverFactory(
    private val integrationMethodProvider: IIntegrationMethodProvider,
): IDaeSystemSolverFactory {
    override fun create(hsmCompilationResult: HsmCompilationResult): DaeSystemStepSolver {
        return DefaultDaeSystemStepSolver(
            integrationMethodProvider.createMethod(),
            hsmCompilationResult.hybridSystem.daeSystem
        )
    }
}