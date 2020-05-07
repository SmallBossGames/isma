package ru.nstu.grin.concatenation.axis.dto

import javafx.scene.paint.Color
import ru.nstu.grin.concatenation.canvas.model.ExistDirection
import java.util.*

data class ConcatenationAxisDTO(
    val id: UUID,
    val name: String,
    val zeroPoint: Double,
    val backGroundColor: Color,
    val delimeterColor: Color,
    val direction: ExistDirection
)