package ru.nstu.grin.concatenation.canvas.model

data class PointToolTipsSettings(
    var isShow: Boolean = false,
    val pointsSettings: MutableSet<PointSettings>
)