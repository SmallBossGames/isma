package ru.nstu.grin.simple.model

import javafx.scene.paint.Color
import ru.nstu.grin.common.model.Point

data class SimpleFunction(
    val name: String,
    val points: List<Point>,
    val color: Color
)