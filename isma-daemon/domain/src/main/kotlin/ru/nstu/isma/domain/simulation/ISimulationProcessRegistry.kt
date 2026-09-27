package ru.nstu.isma.domain.simulation

/**
 * Tracks the runner processes launched for running simulations so they can be
 * terminated on cancellation.
 */
interface ISimulationProcessRegistry {
    fun register(simulationId: Long, process: Process)
    fun unregister(simulationId: Long)
    fun kill(simulationId: Long)
}
