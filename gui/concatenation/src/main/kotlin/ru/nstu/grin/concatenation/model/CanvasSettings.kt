package ru.nstu.grin.concatenation.model

data class CanvasSettings(
    var xCorrelation: Double = 0.0,
    var yCorrelation: Double = 0.0,

    var xPixelCost: Double = 40.0,
    var yPixelCost: Double = 40.0,

    var xStep: Double = 1.0,
    var yStep: Double = 1.0,

    var isXLogarithmic: Boolean = false,
    var isYLogarithmic: Boolean = false,
    var logarithmBase: Double = 10.0
)