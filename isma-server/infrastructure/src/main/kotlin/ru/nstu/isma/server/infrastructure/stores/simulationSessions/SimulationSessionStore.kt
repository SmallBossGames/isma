package ru.nstu.isma.server.infrastructure.stores.simulationSessions

import ru.nstu.isma.domain.simulation.ISimulationSessionStore
import ru.nstu.isma.domain.simulation.SimulationSession
import ru.nstu.isma.domain.simulation.SimulationStatus
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class SimulationSessionStore : ISimulationSessionStore {
    private val sessions = ConcurrentHashMap<Long, SimulationSession>()
    private val nextId = AtomicLong(1L)

    override fun create(session: SimulationSession): Long {
        val id = nextId.getAndIncrement()
        sessions[id] = session
        return id
    }

    override fun get(id: Long): SimulationSession? = sessions[id]

    override fun getAll(): Map<Long, SimulationSession> = sessions.toMap()

    override fun update(id: Long, session: SimulationSession): SimulationSession {
        if (!sessions.containsKey(id)) {
            throw IllegalArgumentException("Session with id '$id' not found")
        }
        sessions[id] = session
        return session
    }

    override fun updateStatus(id: Long, status: SimulationStatus) {
        sessions[id] = sessions[id]!!.copy(status = status)
    }

    override fun updateProgress(id: Long, currentTime: Double) {
        sessions[id] = sessions[id]!!.copy(currentTime = currentTime)
    }

    override fun completeSimulation(id: Long, resultFilePath: String) {
        sessions[id] = sessions[id]!!.copy(
            status = SimulationStatus.COMPLETED,
            resultFilePath = resultFilePath
        )
    }

    override fun failSimulation(id: Long, error: String) {
        sessions[id] = sessions[id]!!.copy(
            status = SimulationStatus.FAILED,
            error = error
        )
    }

    override fun delete(id: Long): Boolean = sessions.remove(id) != null

    override fun exists(id: Long): Boolean = sessions.containsKey(id)
}
