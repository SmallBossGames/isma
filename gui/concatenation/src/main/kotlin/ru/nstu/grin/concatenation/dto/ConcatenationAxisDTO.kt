package ru.nstu.grin.concatenation.dto

import javafx.scene.paint.Color
import ru.nstu.grin.concatenation.model.ExistDirection

data class ConcatenationAxisDTO(
    val name: String,
    val zeroPoint: Double,
    val backGroundColor: Color,
    val delimeterColor: Color,
    val direction: ExistDirection,
    val deltaMarks: List<Double>
)