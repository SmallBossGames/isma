package ru.nstu.isma.domain.handlers.runSimulation

interface IRunSimulationHandler {
    fun handle(parameters: RunSimulationParameters): RunningSimulationResult
}
