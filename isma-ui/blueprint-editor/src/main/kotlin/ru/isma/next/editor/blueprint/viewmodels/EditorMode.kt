package ru.isma.next.editor.blueprint.viewmodels

sealed class EditorMode {
    object Idle : EditorMode()
    data class AddTransition(val selectedStates: MutableList<StateViewModel> = mutableListOf()) : EditorMode()
    object RemoveState : EditorMode()
    object RemoveTransition : EditorMode()
}
