package ru.nstu.grin.dto

import javafx.scene.paint.Color
import ru.nstu.grin.model.Direction
import ru.nstu.grin.model.ExistDirection

data class AxisDTO(
    val color: Color,
    val delimeterColor: Color,
    val direction: ExistDirection,
    val deltaMarks: List<Double>
)