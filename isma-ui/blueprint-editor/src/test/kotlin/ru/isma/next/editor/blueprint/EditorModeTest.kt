package ru.isma.next.editor.blueprint

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.isma.next.editor.blueprint.controls.StateBox

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
        val mode = EditorMode.AddTransition(mutableListOf<StateBox>())
        assertTrue(mode is EditorMode.AddTransition)
        assertEquals(0, mode.selectedStates.size)
    }

    @Test
    fun `AddTransition mode is not not-editing mode`() {
        val mode = EditorMode.AddTransition(mutableListOf<StateBox>())
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
            EditorMode.AddTransition(mutableListOf()),
            EditorMode.RemoveState,
            EditorMode.RemoveTransition
        )

        assertEquals(4, modes.distinct().size)
    }
}
