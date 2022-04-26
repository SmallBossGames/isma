package ru.nstu.grin.concatenation.description.model

import javafx.scene.paint.Color
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace

data class DescriptionDto(
    val space: CartesianSpace,
    val x: Double,
    val y: Double,
    val text: String,
    val textSize: Double,
    val color: Color,
    val font: String,
)