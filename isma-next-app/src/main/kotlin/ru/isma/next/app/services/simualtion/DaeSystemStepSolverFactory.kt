package ru.isma.next.app.services.simualtion

import ru.isma.next.services.simulation.abstractions.models.SimulationParametersModel
import ru.nstu.isma.intg.api.providers.IIntegrationMethodProvider
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver
import ru.nstu.isma.next.core.sim.controller.models.HsmCompilationResult
import ru.nstu.isma.next.core.sim.controller.services.solvers.DefaultDaeSystemStepSolverFactory
import ru.nstu.isma.next.core.sim.controller.services.solvers.IDaeSystemSolverFactory

class DaeSystemStepSolverFactory(
    simulationParameters: SimulationParametersModel,
    private val defaultDaeSystemStepSolverFactory: DefaultDaeSystemStepSolverFactory,
    //private val remoteDaeSystemStepSolverFactory: RemoteDaeSystemStepSolverFactory,
    private val integrationMethodProvider: IIntegrationMethodProvider,
): IDaeSystemSolverFactory {
    private val methodParams = simulationParameters.integrationMethodParameters

    override fun create(hsmCompilationResult: HsmCompilationResult): DaeSystemStepSolver {
        /*return if(methodParams.isParallelInUse){
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
        }*/

        return defaultDaeSystemStepSolverFactory.create(
            integrationMethodProvider.createMethod(),
            hsmCompilationResult.hybridSystem.daeSystem
        )
    }
}