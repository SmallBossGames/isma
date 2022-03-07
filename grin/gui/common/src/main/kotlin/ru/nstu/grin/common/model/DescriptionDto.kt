package ru.nstu.grin.common.model

import javafx.scene.paint.Color

data class DescriptionDto(
    val x: Double,
    val y: Double,
    val text: String,
    val textSize: Double,
    val color: Color,
    val font: String,
)