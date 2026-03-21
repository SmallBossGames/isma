package ru.nstu.isma.domain.handlers.runSimulation

import ru.nstu.isma.domain.simulation.ISimulationExecutor
import ru.nstu.isma.domain.simulation.ISimulationSessionStore

class RunSimulationHandlerImpl(
    private val lismaTranslator: ILismaTranslator,
    private val sessionStore: ISimulationSessionStore,
    private val simulationExecutor: ISimulationExecutor,
) : IRunSimulationHandler {
    
    override fun handle(parameters: RunSimulationParameters): RunningSimulationResult {
        val hsmResult = lismaTranslator.translate(parameters.lismaSourceCode)
        val hsm = hsmResult.getOrElse {
            throw IllegalArgumentException("LISMA translation failed: ${it.message}")
        }

        val session = sessionStore.create(parameters.startTime, parameters.endTime)

        simulationExecutor.execute(session.simulationId, parameters, hsm)

        return RunningSimulationResult(session.simulationId)
    }
}
