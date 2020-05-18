package ru.nstu.grin.concatenation.axis.model

data class AxisSettings(
    var correlation: Double = 0.0,

    var pixelCost: Double = 40.0,

    var step: Double = 1.0,

    var min: Double = -14.0,
    var max: Double = 14.0,

    var isOnlyIntegerPow: Boolean = false,
    var isLogarithmic: Boolean = false,
    var logarithmBase: Double = 10.0
)