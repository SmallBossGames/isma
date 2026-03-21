package ru.nstu.isma.domain.handlers.runSimulation

import ru.nstu.isma.domain.simulation.ISimulationExecutor
import ru.nstu.isma.domain.simulation.ISimulationSessionStore
import ru.nstu.isma.domain.simulation.SimulationSession

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

        val session = SimulationSession(
            simulationId = 0L,
            startTime = parameters.startTime,
            endTime = parameters.endTime,
        )
        val simulationId = sessionStore.create(session)

        simulationExecutor.execute(simulationId, parameters, hsm)

        return RunningSimulationResult(simulationId)
    }
}
