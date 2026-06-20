package ru.isma.next.editor.blueprint

import ru.isma.next.editor.blueprint.controls.StateBox

sealed class EditorMode {
    object Idle : EditorMode()
    data class AddTransition(val selectedStates: MutableList<StateBox> = mutableListOf()) : EditorMode()
    object RemoveState : EditorMode()
    object RemoveTransition : EditorMode()
}

fun EditorMode.isNotEditingMode(): Boolean = this !is EditorMode.RemoveState && this !is EditorMode.AddTransition
