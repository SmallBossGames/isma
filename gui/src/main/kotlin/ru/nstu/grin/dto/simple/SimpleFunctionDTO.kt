package ru.nstu.grin.dto.simple

import javafx.scene.paint.Color
import ru.nstu.grin.model.Point

data class SimpleFunctionDTO(
    val name: String,
    val points: List<Point>,
    val functionColor: Color
)