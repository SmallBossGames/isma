package ru.nstu.grin.concatenation.function.dto

import javafx.scene.paint.Color
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.function.model.LineType
import java.util.*

data class ConcatenationFunctionDTO(
    val id: UUID,
    val name: String,
    val points: List<Point>,
    val functionColor: Color,
    val lineSize: Double,
    val lineType: LineType
)