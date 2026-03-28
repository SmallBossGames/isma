package ru.nstu.isma.domain.handlers.cancelSimulation

import ru.nstu.isma.domain.simulation.ISimulationSessionStore
import ru.nstu.isma.domain.simulation.SimulationStatus

class CancelSimulationHandlerImpl(
    private val sessionStore: ISimulationSessionStore,
) : ICancelSimulationHandler {

    override fun handle(simulationId: Long) {
        if (!sessionStore.exists(simulationId)) {
            throw IllegalArgumentException("Simulation with id '$simulationId' not found")
        }

        val session = sessionStore.get(simulationId)
        if (session?.status != SimulationStatus.RUNNING) {
            throw IllegalStateException("Simulation is not running")
        }

        sessionStore.updateStatus(simulationId, SimulationStatus.CANCELLED)
    }
}
