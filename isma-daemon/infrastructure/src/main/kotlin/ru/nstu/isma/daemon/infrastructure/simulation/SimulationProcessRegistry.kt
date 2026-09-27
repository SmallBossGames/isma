package ru.nstu.isma.daemon.infrastructure.simulation

import ru.nstu.isma.domain.simulation.ISimulationProcessRegistry
import java.util.concurrent.ConcurrentHashMap

/**
 * In-memory registry of the runner processes launched for running simulations.
 * Used to terminate a runner process when its simulation is cancelled.
 */
class SimulationProcessRegistry : ISimulationProcessRegistry {

    private val processes = ConcurrentHashMap<Long, Process>()

    override fun register(simulationId: Long, process: Process) {
        processes[simulationId] = process
    }

    override fun unregister(simulationId: Long) {
        processes.remove(simulationId)
    }

    override fun kill(simulationId: Long) {
        processes[simulationId]?.destroy()
    }
}
