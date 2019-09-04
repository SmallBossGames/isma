package ru.nstu.grin.model

import javafx.scene.paint.Color
import ru.nstu.grin.MappingPosition

/**
 * @author kostya05983
 */
data class Function(
    val pointArray: List<Point>,
    val xDirection: MappingPosition,
    val yDirection: MappingPosition,
    val minDelta: Double,
    val delta: Double,
    val functionColor: Color,
    val xAxisColor: Color,
    val yAxisColor: Color
)