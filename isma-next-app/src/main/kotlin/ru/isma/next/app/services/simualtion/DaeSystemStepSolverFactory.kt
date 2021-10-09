package ru.isma.next.app.services.simualtion

import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver
import ru.nstu.isma.next.core.sim.controller.models.HsmCompilationResult
import ru.nstu.isma.next.core.sim.controller.services.IIntegrationMethodProvider
import ru.nstu.isma.next.core.sim.controller.services.solvers.DefaultDaeSystemStepSolverFactory
import ru.nstu.isma.next.core.sim.controller.services.solvers.IDaeSystemSolverFactory
import ru.nstu.isma.next.core.sim.controller.services.solvers.RemoteDaeSystemStepSolverFactory

class DaeSystemStepSolverFactory(
    private val parametersService: SimulationParametersService,
    private val defaultDaeSystemStepSolverFactory: DefaultDaeSystemStepSolverFactory,
    private val remoteDaeSystemStepSolverFactory: RemoteDaeSystemStepSolverFactory,
    private val integrationMethodProvider: IIntegrationMethodProvider,
): IDaeSystemSolverFactory {
    override fun create(hsmCompilationResult: HsmCompilationResult): DaeSystemStepSolver {
        val methodParams = parametersService.integrationMethod
        return if(methodParams.isParallelInUse){
            remoteDaeSystemStepSolverFactory.create(
                methodParams.server,
                methodParams.port,
                integrationMethodProvider.createMethod(),
                hsmCompilationResult
            )
        } else {
            defaultDaeSystemStepSolverFactory.create(
                integrationMethodProvider.createMethod(),
                hsmCompilationResult.hybridSystem.daeSystem
            )
        }
    }
}