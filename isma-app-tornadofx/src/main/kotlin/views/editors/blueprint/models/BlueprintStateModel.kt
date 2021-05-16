package views.editors.blueprint.models

import kotlinx.serialization.Serializable

@Serializable
data class BlueprintStateModel(
    val canvasPositionX: Double,
    val canvasPositionY: Double,
    val name: String,
    val text: String
)