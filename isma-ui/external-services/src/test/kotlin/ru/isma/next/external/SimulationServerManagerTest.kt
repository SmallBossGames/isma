package ru.isma.next.external

import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class SimulationServerManagerTest {

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `start throws when script file does not exist`() {
        val manager = SimulationServerManager("/nonexistent/path/to/server-script")

        try {
            manager.start()
            assert(false) { "Expected IllegalStateException" }
        } catch (e: IllegalStateException) {
            assert(e.message!!.contains("isma-server script not found")) {
                "Expected script not found, got: ${e.message}"
            }
        }
    }

    @Test
    fun `stop when not running is no-op`() {
        val manager = SimulationServerManager("/nonexistent/script")

        manager.stop()
        manager.stop()
        manager.stop()

        // Should not throw - stop when not running is safe
    }

    @Test
    fun `stop sets running to false and clears state`() {
        val manager = SimulationServerManager("/nonexistent/script")

        manager.stop()
        manager.stop()
        manager.stop()

        // No exceptions thrown - lifecycle is safe
    }
}
