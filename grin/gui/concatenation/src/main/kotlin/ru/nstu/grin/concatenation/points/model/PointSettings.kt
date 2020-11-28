package ru.nstu.grin.concatenation.points.model

import ru.nstu.grin.concatenation.axis.model.AxisSettings

data class PointSettings(
    var xAxisSettings: AxisSettings,
    var yAxisSettings: AxisSettings,
    var xGraphic: Double,
    var yGraphic: Double
)