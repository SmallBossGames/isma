package ru.nstu.grin.concatenation.axis.model

data class AxisSettings(
    var isOnlyIntegerPow: Boolean = false,
    var integerStep: Int = 1,

    var isLogarithmic: Boolean = false,
    var logarithmBase: Double = 10.0,

    var min: Double = 0.0,
    var max: Double = 10.0,
)



