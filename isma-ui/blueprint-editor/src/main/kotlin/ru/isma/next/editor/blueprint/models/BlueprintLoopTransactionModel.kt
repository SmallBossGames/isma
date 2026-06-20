package ru.isma.next.editor.blueprint.models

import kotlinx.serialization.Serializable

@Serializable
class BlueprintLoopTransactionModel(
    val stateName: String,
    val predicate: String,
    val alias: String = "",
    val text: String
)
