package ru.nstu.grin.dto

import javafx.scene.paint.Color
import ru.nstu.grin.model.DrawSize
import ru.nstu.grin.model.Point

data class FunctionDTO(
    val name: String,
    val points: List<Point>,
    val xAxis: AxisDTO,
    val yAxis: AxisDTO,
    val functionColor: Color
)