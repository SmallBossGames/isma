package ru.nstu.grin.concatenation.model

data class ScaleSettings(
    var upRemaining: Long = 5L,
    var downRemaining: Long = 5L,
    var delta: Double
)