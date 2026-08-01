package ru.isma.next.editor.blueprint.viewmodels

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EditorModeTest {

    @Test
    fun `Idle mode is not editing mode`() {
        val mode: EditorMode = EditorMode.Idle
        assertFalse(mode is EditorMode.RemoveState)
        assertFalse(mode is EditorMode.AddTransition)
        assertTrue(mode.isNotEditingMode())
    }

    @Test
    fun `RemoveState mode is correctly identified`() {
        val mode: EditorMode = EditorMode.RemoveState
        assertTrue(mode is EditorMode.RemoveState)
        assertFalse(mode is EditorMode.AddTransition)
        assertFalse(mode.isNotEditingMode())
    }

    @Test
    fun `AddTransition mode carries selected states`() {
        val mode: EditorMode.AddTransition = EditorMode.AddTransition(mutableSetOf())
        assertEquals(0, mode.selectedStates.size)
    }

    @Test
    fun `AddTransition mode is not not-editing mode`() {
        val mode = EditorMode.AddTransition(mutableSetOf())
        assertFalse(mode.isNotEditingMode())
    }

    @Test
    fun `RemoveTransition mode is correctly identified`() {
        val mode: EditorMode = EditorMode.RemoveTransition
        assertTrue(mode is EditorMode.RemoveTransition)
        assertTrue(mode.isNotEditingMode())
    }

    @Test
    fun `modes are mutually exclusive by type`() {
        val modes: List<EditorMode> = listOf(
            EditorMode.Idle,
            EditorMode.AddTransition(mutableSetOf()),
            EditorMode.RemoveState,
            EditorMode.RemoveTransition
        )

        assertEquals(4, modes.distinct().size)
    }

    @Test
    fun `AddTransition mode can add and check states`() {
        val state1 = StateViewModel(name = "State 1")
        val state2 = StateViewModel(name = "State 2")
        val mode = EditorMode.AddTransition(mutableSetOf(state1))
        mode.selectedStates.add(state2)
        assertTrue(mode.selectedStates.contains(state1))
        assertTrue(mode.selectedStates.contains(state2))
        assertEquals(2, mode.selectedStates.size)
    }
}
