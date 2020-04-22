package ru.nstu.grin.simple.dto

import javafx.scene.paint.Color
import ru.nstu.grin.common.model.Point

data class SimpleFunctionDTO(
    val name: String,
    val points: List<Point>,
    val color: Color,
    val step: Int
)