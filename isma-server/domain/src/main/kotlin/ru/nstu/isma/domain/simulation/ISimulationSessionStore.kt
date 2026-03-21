package ru.nstu.isma.domain.simulation

interface ISimulationSessionStore {
    /**
     * Creates a new simulation session and returns its unique identifier.
     * @param session the session data to store
     * @return auto-generated unique id of the created session
     */
    fun create(session: SimulationSession): Long

    /**
     * Retrieves a simulation session by its id.
     * @param id unique identifier of the session
     * @return the session or null if not found
     */
    fun get(id: Long): SimulationSession?

    /**
     * Returns all stored simulation sessions as a map of id to session.
     * @return map of all sessions
     */
    fun getAll(): Map<Long, SimulationSession>

    /**
     * Updates an existing simulation session.
     * @param id unique identifier of the session to update
     * @param session new session data
     * @return the updated session
     * @throws IllegalArgumentException if session with given id does not exist
     */
    fun update(id: Long, session: SimulationSession): SimulationSession

    /**
     * Deletes a simulation session by its id.
     * @param id unique identifier of the session to delete
     * @return true if session was deleted, false if it did not exist
     */
    fun delete(id: Long): Boolean

    /**
     * Checks if a simulation session exists.
     * @param id unique identifier to check
     * @return true if session exists, false otherwise
     */
    fun exists(id: Long): Boolean
}
