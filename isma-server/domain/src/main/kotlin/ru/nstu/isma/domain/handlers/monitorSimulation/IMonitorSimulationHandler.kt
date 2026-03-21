package ru.nstu.isma.domain.handlers.monitorSimulation

interface IMonitorSimulationHandler {
    fun handle(simulationId: Long, accuracy: Double, onProgress: (SimulationProgress) -> Unit)
}
