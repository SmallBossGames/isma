package ru.nstu.grin.model

import javafx.scene.paint.Color
import ru.nstu.grin.MappingPosition

data class Axis(
    val direction: MappingPosition,
    val backColor: Color,
    val deltaColor: Color
)