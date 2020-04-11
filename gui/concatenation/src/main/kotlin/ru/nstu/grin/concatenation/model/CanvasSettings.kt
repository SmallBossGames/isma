package ru.nstu.grin.concatenation.model

data class CanvasSettings(
    var xCorrelation: Double = 0.0,
    var yCorrelation: Double = 0.0,
    var pixelCost: Double = 40.0,
    var step: Double = 1.0
)