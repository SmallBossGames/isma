package ru.nstu.isma.domain.simulation

interface ISimulationSessionStore {
    fun create(session: SimulationSession): Long

    fun get(id: Long): SimulationSession?

    fun getAll(): Map<Long, SimulationSession>

    fun update(id: Long, session: SimulationSession): SimulationSession

    fun updateStatus(id: Long, status: SimulationStatus)

    fun updateProgress(id: Long, currentTime: Double)

    fun completeSimulation(id: Long, resultFilePath: String)

    fun failSimulation(id: Long, error: String)

    fun delete(id: Long): Boolean

    fun exists(id: Long): Boolean
}
