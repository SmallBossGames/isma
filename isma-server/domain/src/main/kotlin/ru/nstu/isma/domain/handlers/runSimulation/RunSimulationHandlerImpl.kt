package ru.nstu.isma.domain.handlers.runSimulation

import ru.nstu.isma.domain.compiler.ICompiledModelStore
import ru.nstu.isma.domain.simulation.ISimulationExecutor
import ru.nstu.isma.domain.simulation.ISimulationSessionStore

class RunSimulationHandlerImpl(
    private val compiledModelStore: ICompiledModelStore,
    private val sessionStore: ISimulationSessionStore,
    private val simulationExecutor: ISimulationExecutor,
) : IRunSimulationHandler {
    
    override fun handle(parameters: RunSimulationParameters): RunningSimulationResult {
        val hsm = compiledModelStore.get(parameters.compiledModelId)
            ?: throw IllegalArgumentException("Compiled model not found: ${parameters.compiledModelId}")

        val session = sessionStore.create(parameters.startTime, parameters.endTime)

        simulationExecutor.execute(session.simulationId, parameters, hsm)

        return RunningSimulationResult(session.simulationId)
    }
}
