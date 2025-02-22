package ru.isma.next.editor.blueprint.models

import kotlinx.serialization.Serializable

@Serializable
data class BlueprintStateModel(
    val canvasPositionX: Double,
    val canvasPositionY: Double,
    val name: String,
    val text: String
)