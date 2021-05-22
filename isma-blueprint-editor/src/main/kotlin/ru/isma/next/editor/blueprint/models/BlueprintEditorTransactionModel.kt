package ru.isma.next.editor.blueprint.models

import ru.isma.next.editor.blueprint.controls.StateBox
import ru.isma.next.editor.blueprint.controls.StateTransactionArrow

data class BlueprintEditorTransactionModel(
    val startStateBox: StateBox,
    val endStateBox: StateBox,
    val transactionArrow: StateTransactionArrow
)
