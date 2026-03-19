package ru.nstu.isma.domain.handlers.monitorSimulation

interface IMonitorSimulationHandler {
    fun handle(simulationId: String, onProgress: (SimulationProgress) -> Unit)
}
