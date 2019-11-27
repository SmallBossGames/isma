package ru.nstu.grin.dto

import javafx.scene.paint.Color
import ru.nstu.grin.Direction

data class AxisDTO(
    val color: Color,
    val delimeterColor: Color,
    val direction: Direction
)