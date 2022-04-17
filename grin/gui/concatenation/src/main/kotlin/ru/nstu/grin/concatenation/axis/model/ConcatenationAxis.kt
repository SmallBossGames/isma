package ru.nstu.grin.concatenation.axis.model

enum class Direction { LEFT, RIGHT, TOP, BOTTOM }

data class ConcatenationAxis(
    val name: String,
    val direction: Direction,
    var styleProperties: AxisStyleProperties = AxisStyleProperties(),
    var scaleProperties: AxisScaleProperties = AxisScaleProperties(),
)