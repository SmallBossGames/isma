package ru.nstu.grin.concatenation.description.model

import javafx.scene.paint.Color
import ru.nstu.grin.common.model.Description

data class UpdateDescriptionModel(
    val description: Description,
    val text: String,
    val textSize: Double,
    val color: Color,
    val font: String
)