package ru.isma.next.editor.blueprint.viewmodels

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EditorModeTest {

    @Test
    fun `Idle mode is correctly identified`() {
        val mode: EditorMode = EditorMode.Idle
        assertFalse(mode is EditorMode.RemoveState)
        assertFalse(mode is EditorMode.AddTransition)
    }

    @Test
    fun `RemoveState mode is correctly identified`() {
        val mode: EditorMode = EditorMode.RemoveState
        assertTrue(mode is EditorMode.RemoveState)
        assertFalse(mode is EditorMode.AddTransition)
    }

    @Test
    fun `AddTransition mode carries selected states`() {
        val mode: EditorMode.AddTransition = EditorMode.AddTransition(mutableListOf())
        assertEquals(0, mode.selectedStates.size)
    }

    @Test
    fun `RemoveTransition mode is correctly identified`() {
        val mode: EditorMode = EditorMode.RemoveTransition
        assertTrue(mode is EditorMode.RemoveTransition)
    }

    @Test
    fun `modes are mutually exclusive by type`() {
        val modes: List<EditorMode> = listOf(
            EditorMode.Idle,
            EditorMode.AddTransition(mutableListOf()),
            EditorMode.RemoveState,
            EditorMode.RemoveTransition
        )

        assertEquals(4, modes.distinct().size)
    }

    @Test
    fun `AddTransition mode can add and check states`() {
        val state1 = StateViewModel(name = "State 1")
        val state2 = StateViewModel(name = "State 2")
        val mode = EditorMode.AddTransition(mutableListOf(state1))
        mode.selectedStates.add(state2)
        assertTrue(mode.selectedStates.contains(state1))
        assertTrue(mode.selectedStates.contains(state2))
        assertEquals(2, mode.selectedStates.size)
    }
}
