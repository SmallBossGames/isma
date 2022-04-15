package ru.nstu.grin.concatenation.axis.model

enum class AxisScalingType { LINEAR, LOGARITHMIC }

data class AxisScaleProperties(
    val scalingType: AxisScalingType = AxisScalingType.LINEAR,
    val scalingLogBase: Double = 10.0,

    val minValue: Double = 0.0,
    val maxValue: Double = 10.0,
)