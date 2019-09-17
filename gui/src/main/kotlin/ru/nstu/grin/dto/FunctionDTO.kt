package ru.nstu.grin.dto

import javafx.scene.paint.Color
import ru.nstu.grin.Direction
import ru.nstu.grin.model.Point

data class FunctionDTO(
    val points: List<Point>,
    val minX: Double,
    val maxX: Double,
    val minY: Double,
    val maxY: Double,
    val xDirection: Direction,
    val yDirection: Direction,
    val functionColor: Color,
    val xAxisColor: Color,
    val yAxisColor: Color
)