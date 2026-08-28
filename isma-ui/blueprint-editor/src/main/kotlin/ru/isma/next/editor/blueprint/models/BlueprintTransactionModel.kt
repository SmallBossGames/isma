package ru.isma.next.editor.blueprint.models

class BlueprintTransactionModel (
    val startStateName: String,
    val endStateName: String,
    val predicate: String,
    val alias: String = "",
)