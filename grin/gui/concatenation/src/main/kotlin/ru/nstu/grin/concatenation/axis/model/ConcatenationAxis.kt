package ru.nstu.grin.concatenation.axis.model

enum class Direction { LEFT, RIGHT, TOP, BOTTOM }

data class ConcatenationAxis(
    var name: String,
    var direction: Direction,
    var styleProperties: AxisStyleProperties = AxisStyleProperties(),
    var scaleProperties: AxisScaleProperties = AxisScaleProperties(),
)