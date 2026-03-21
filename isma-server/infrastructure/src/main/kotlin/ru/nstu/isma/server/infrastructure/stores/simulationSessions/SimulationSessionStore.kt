package ru.nstu.isma.server.infrastructure.stores.simulationSessions

import ru.nstu.isma.domain.simulation.ISimulationSessionStore
import ru.nstu.isma.domain.simulation.SimulationSession
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * Thread-safe in-memory key-value storage for simulation sessions.
 * Uses auto-incrementing numeric ids for session identification.
 */
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

    override fun delete(id: Long): Boolean = sessions.remove(id) != null

    override fun exists(id: Long): Boolean = sessions.containsKey(id)
}
