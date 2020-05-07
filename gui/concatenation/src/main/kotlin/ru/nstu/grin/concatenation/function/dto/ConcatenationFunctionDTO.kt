package ru.nstu.grin.concatenation.function.dto

import javafx.scene.paint.Color
import ru.nstu.grin.common.model.Point
import java.util.*

data class ConcatenationFunctionDTO(
    val id: UUID,
    val name: String,
    val points: List<Point>,
    val functionColor: Color
)