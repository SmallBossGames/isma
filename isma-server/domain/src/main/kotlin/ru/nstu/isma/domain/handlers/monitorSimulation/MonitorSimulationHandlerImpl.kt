package ru.nstu.isma.domain.handlers.monitorSimulation

import ru.nstu.isma.domain.simulation.ISimulationSessionStore
import ru.nstu.isma.domain.simulation.SimulationStatus

class MonitorSimulationHandlerImpl(
    private val sessionStore: ISimulationSessionStore,
) : IMonitorSimulationHandler {

    private val pollIntervalMs = 100L

    override fun handle(simulationId: Long, accuracy: Double, onProgress: (SimulationProgress) -> Unit) {
        val session = sessionStore.get(simulationId)
            ?: throw IllegalArgumentException("Simulation with id '$simulationId' not found")

        val timeRange = session.endTime - session.startTime
        val threshold = if (accuracy > 0.0 && timeRange > 0.0) accuracy * timeRange else 0.0

        var lastReportedTime = session.currentTime

        while (true) {
            val currentSession = sessionStore.get(simulationId)
                ?: throw IllegalStateException("Simulation session '$simulationId' disappeared")

            val currentTime = currentSession.currentTime

            val isFinal = currentSession.status != SimulationStatus.RUNNING
            val shouldReport = isFinal || (threshold > 0.0 && (currentTime - lastReportedTime) >= threshold)

            if (shouldReport) {
                onProgress(SimulationProgress(
                    startTime = currentSession.startTime,
                    endTime = currentSession.endTime,
                    currentTime = currentTime,
                ))
                lastReportedTime = currentTime
            }

            if (isFinal) break

            Thread.sleep(pollIntervalMs)
        }
    }
}
