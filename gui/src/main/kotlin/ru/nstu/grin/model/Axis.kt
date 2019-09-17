package ru.nstu.grin.model

import javafx.scene.paint.Color
import ru.nstu.grin.Direction

data class Axis(
    val direction: Direction,
    val backColor: Color,
    val deltaColor: Color
)