package ru.isma.next.editor.blueprint.models

import ru.isma.next.editor.blueprint.controls.StateBox
import ru.isma.next.editor.blueprint.controls.TransactionArrow

data class BlueprintEditorTransactionModel(
    val startStateBox: StateBox,
    val endStateBox: StateBox,
    val transactionArrow: TransactionArrow
)
