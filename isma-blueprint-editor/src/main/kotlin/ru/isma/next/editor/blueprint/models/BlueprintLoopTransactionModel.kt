package ru.isma.next.editor.blueprint.models

import kotlinx.serialization.Serializable
import ru.isma.next.editor.blueprint.controls.LoopTransactionArrow
import ru.isma.next.editor.blueprint.controls.StateBox

@Serializable
class BlueprintLoopTransactionModel(
    val stateName: String,
    val predicate: String,
    val alias: String = "",
    val text: String
)

data class BlueprintEditorLoopTransactionModel(
    val stateBox: StateBox,
    val loopTransactionArrow: LoopTransactionArrow,
)