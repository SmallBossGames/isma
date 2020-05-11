package ru.nstu.grin.concatenation.canvas.model

import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis

data class TraceSettings(
    var pressedPoint: Point,
    var xAxis: ConcatenationAxis,
    var yAxis: ConcatenationAxis
)