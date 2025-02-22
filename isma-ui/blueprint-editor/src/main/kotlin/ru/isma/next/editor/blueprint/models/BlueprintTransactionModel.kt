package ru.isma.next.editor.blueprint.models

import kotlinx.serialization.Serializable

@Serializable
class BlueprintTransactionModel (
    val startStateName: String,
    val endStateName: String,
    val predicate: String,
    val alias: String = "",
)