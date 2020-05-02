package ru.nstu.grin.common.model

import ru.nstu.grin.common.model.PointSettings

data class PointToolTipsSettings(
    var isShow: Boolean = false,
    val pointsSettings: MutableSet<PointSettings>
)