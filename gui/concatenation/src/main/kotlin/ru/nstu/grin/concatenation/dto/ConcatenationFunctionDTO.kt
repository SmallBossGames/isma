package ru.nstu.grin.concatenation.dto

import javafx.scene.paint.Color
import ru.nstu.grin.common.model.Point

data class ConcatenationFunctionDTO(
    val name: String,
    val points: List<Point>,
    val functionColor: Color
)