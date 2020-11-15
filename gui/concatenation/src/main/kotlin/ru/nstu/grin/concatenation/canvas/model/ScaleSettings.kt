package ru.nstu.grin.concatenation.canvas.model

data class ScaleSettings(
    var upRemaining: Long = 5L,
    var downRemaining: Long = 5L,
    var delta: Double
)