package ru.nstu.grin.concatenation.points.model

data class PointToolTipsSettings(
    var isShow: Boolean = false,
    val pointsSettings: MutableSet<PointSettings>
)