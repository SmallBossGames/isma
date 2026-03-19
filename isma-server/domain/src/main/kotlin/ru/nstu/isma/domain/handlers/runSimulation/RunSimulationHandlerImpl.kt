package ru.nstu.isma.domain.handlers.runSimulation

import java.util.UUID

class RunSimulationHandlerImpl : IRunSimulationHandler {
    override fun handle(parameters: RunSimulationParameters): RunningSimulationResult {
        val simulationId = UUID.randomUUID().toString()
        return RunningSimulationResult(simulationId)
    }
}
