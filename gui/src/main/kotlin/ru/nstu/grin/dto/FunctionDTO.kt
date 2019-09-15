package ru.nstu.grin.dto

import javafx.scene.effect.Light
import javafx.scene.paint.Color
import ru.nstu.grin.MappingPosition
import ru.nstu.grin.model.Point
import java.nio.DoubleBuffer

data class FunctionDTO(
    val points: List<Point>,
    val minX: Double,
    val maxX: Double,
    val minY: Double,
    val maxY: Double,
    val xDirection: MappingPosition,
    val yDirection: MappingPosition,
    val functionColor: Color,
    val xAxisColor: Color,
    val yAxisColor: Color
)