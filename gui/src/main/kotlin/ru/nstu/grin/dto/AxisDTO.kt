package ru.nstu.grin.dto

import javafx.scene.paint.Color
import ru.nstu.grin.model.Direction

data class AxisDTO(
    val color: Color,
    val delimeterColor: Color,
    val direction: Direction
)