package ru.isma.next.editor.blueprint.models

import kotlinx.serialization.Serializable

@Serializable
class BlueprintLoopTransactionModel(
    val stateBox: BlueprintStateModel,
    val predicate: String,
    val text: String
)