package ru.nstu.grin.concatenation.points.model

import ru.nstu.grin.concatenation.axis.model.AxisScaleProperties

data class PointSettings(
    var xAxisProperties: AxisScaleProperties,
    var yAxisProperties: AxisScaleProperties,
    var xGraphic: Double,
    var yGraphic: Double
)