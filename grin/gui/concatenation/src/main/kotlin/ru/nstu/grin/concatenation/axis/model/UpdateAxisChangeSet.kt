package ru.nstu.grin.concatenation.axis.model

import javafx.scene.paint.Color

data class UpdateAxisChangeSet(
    val distance: Double,
    val textSize: Double,
    val font: String,
    val fontColor: Color,
    val axisColor: Color,
    val isHide: Boolean,
    val axisMarkType: AxisMarkType,
    val min: Double,
    val max: Double
)

