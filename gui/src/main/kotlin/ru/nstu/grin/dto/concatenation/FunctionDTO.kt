package ru.nstu.grin.dto.concatenation

import javafx.scene.paint.Color
import ru.nstu.grin.model.Point

data class FunctionDTO(
    val name: String,
    val points: List<Point>,
    val xAxis: AxisDTO,
    val yAxis: AxisDTO,
    val functionColor: Color
)