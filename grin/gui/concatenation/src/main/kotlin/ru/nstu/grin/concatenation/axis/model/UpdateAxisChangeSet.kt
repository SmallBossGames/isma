package ru.nstu.grin.concatenation.axis.model

data class UpdateAxisChangeSet(
    val name: String,
    val direction: Direction,
    val styleProperties: AxisStyleProperties,
    val scaleProperties: AxisScaleProperties,
)