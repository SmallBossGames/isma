package ru.nstu.grin.simple.view

data class SimplePlotSettings(
    var pixelCost: Double,

    var xCorrelation: Double = 0.0,
    var yCorrelation: Double = 0.0,

    var step: Double,

    var isXLogarithmic: Boolean = false,
    var isYLogarithmic: Boolean = false,

    var logarithmBase: Double = 10.0
)