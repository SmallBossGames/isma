package ru.nstu.grin.common.model

data class PointToolTipsSettings(
    var isShow: Boolean = false,
    val pointsSettings: MutableSet<PointSettings>
)