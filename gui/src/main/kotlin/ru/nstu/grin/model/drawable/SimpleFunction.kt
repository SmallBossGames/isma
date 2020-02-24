package ru.nstu.grin.model.drawable

import javafx.scene.paint.Color
import ru.nstu.grin.model.Point

data class SimpleFunction(
    val name: String,
    val points: List<Point>,
    val color: Color
)