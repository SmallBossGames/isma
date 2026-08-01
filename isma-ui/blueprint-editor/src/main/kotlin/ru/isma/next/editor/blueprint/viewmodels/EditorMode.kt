package ru.isma.next.editor.blueprint.viewmodels

sealed class EditorMode {
    object Idle : EditorMode()
    data class AddTransition(val selectedStates: MutableSet<StateViewModel> = mutableSetOf()) : EditorMode()
    object RemoveState : EditorMode()
    object RemoveTransition : EditorMode()
}

fun EditorMode.isNotEditingMode(): Boolean = this !is EditorMode.RemoveState && this !is EditorMode.AddTransition
