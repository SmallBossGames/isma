package ru.nstu.grin.concatenation.axis.dto

import javafx.scene.paint.Color
import ru.nstu.grin.concatenation.canvas.model.ExistDirection

data class ConcatenationAxisDTO(
    val name: String,
    val zeroPoint: Double,
    val backGroundColor: Color,
    val delimeterColor: Color,
    val direction: ExistDirection
)