package ru.isma.next.editor.blueprint.viewmodels

sealed class BlueprintEvent {
    data class OpenStateEditor(val state: StateViewModel) : BlueprintEvent()
    data class OpenLoopEditor(val loop: LoopTransactionViewModel, val state: StateViewModel) : BlueprintEvent()
}
