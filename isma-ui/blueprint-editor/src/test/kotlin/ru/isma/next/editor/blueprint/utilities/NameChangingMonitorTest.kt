package ru.isma.next.editor.blueprint.utilities

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NameChangingMonitorTest {
    private lateinit var monitor: NameChangingMonitor

    @BeforeEach
    fun setUp() {
        monitor = NameChangingMonitor("New state")
    }

    @Test
    fun `first registration succeeds`() {
        assertTrue(monitor.tryRegister("New state 1"))
    }

    @Test
    fun `unique name registration succeeds`() {
        assertTrue(monitor.tryRegister("MyState"))
    }

    @Test
    fun `duplicate registration fails`() {
        monitor.tryRegister("New state 1")
        assertFalse(monitor.tryRegister("New state 1"))
    }

    @Test
    fun `unregister allows re-registration`() {
        monitor.tryRegister("New state 1")
        assertTrue(monitor.tryUnregister("New state 1"))
        assertTrue(monitor.tryRegister("New state 1"))
    }

    @Test
    fun `unregister unknown name fails`() {
        assertFalse(monitor.tryUnregister("NonExistent"))
    }

    @Test
    fun `counter increments on high-numbered name`() {
        monitor.tryRegister("New state 5")
        assertEquals("New state 6", monitor.createNextDefaultName())
    }

    @Test
    fun `counter does not decrease on lower name`() {
        monitor.tryRegister("New state 7")
        monitor.tryRegister("New state 3")
        assertEquals("New state 8", monitor.createNextDefaultName())
    }

    @Test
    fun `non-default names do not affect counter`() {
        monitor.tryRegister("MyState")
        assertEquals("New state 1", monitor.createNextDefaultName())
    }

    @Test
    fun `counter persists across unregister of high number`() {
        monitor.tryRegister("New state 10")
        monitor.tryUnregister("New state 10")
        assertEquals("New state 11", monitor.createNextDefaultName())
    }

    @Test
    fun `default name regex matches only correct format`() {
        monitor.tryRegister("New state 42")
        assertEquals("New state 43", monitor.createNextDefaultName())

        monitor.tryRegister("New state")
        monitor.tryRegister("New state abc")
        monitor.tryRegister("Newstate 5")
        assertEquals("New state 43", monitor.createNextDefaultName())
    }

    @Test
    fun `multiple states can coexist`() {
        assertTrue(monitor.tryRegister("State A"))
        assertTrue(monitor.tryRegister("State B"))
        assertTrue(monitor.tryRegister("State C"))
        assertEquals("New state 1", monitor.createNextDefaultName())
    }

    @Test
    fun `counter recovers from gap`() {
        monitor.tryRegister("New state 1")
        monitor.tryRegister("New state 3")
        monitor.tryUnregister("New state 1")
        assertEquals("New state 4", monitor.createNextDefaultName())
    }
}
