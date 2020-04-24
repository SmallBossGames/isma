package ru.nstu.grin.concatenation.model

data class AxisSettings(
    var correlation: Double = 0.0,

    var pixelCost: Double = 40.0,

    var step: Double = 1.0,

    var isLogarithmic: Boolean = false,
    var logarithmBase: Double = 10.0
)