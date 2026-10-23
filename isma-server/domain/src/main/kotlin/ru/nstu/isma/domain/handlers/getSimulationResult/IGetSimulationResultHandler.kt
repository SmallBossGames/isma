package ru.nstu.isma.domain.handlers.getSimulationResult

interface IGetSimulationResultHandler {
    fun handle(simulationId: Long): String
}
