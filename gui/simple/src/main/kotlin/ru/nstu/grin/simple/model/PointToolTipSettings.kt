package ru.nstu.grin.simple.model

data class PointToolTipSettings(
    var isShow: Boolean = false,
    var pointsSettings: MutableSet<PointSettings>
)