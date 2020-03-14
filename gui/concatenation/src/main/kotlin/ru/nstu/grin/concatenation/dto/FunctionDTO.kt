package ru.nstu.grin.concatenation.dto

import javafx.scene.paint.Color
import ru.nstu.grin.common.model.Point

data class FunctionDTO(
    val name: String,
    val points: List<Point>,
    val xAxis: AxisDTO,
    val yAxis: AxisDTO,
    val functionColor: Color
)