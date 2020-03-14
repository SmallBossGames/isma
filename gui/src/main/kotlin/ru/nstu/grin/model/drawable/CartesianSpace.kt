package ru.nstu.grin.model.drawable

import ru.nstu.grin.model.CanvasSettings
import ru.nstu.grin.model.drawable.axis.AbstractAxis

data class CartesianSpace(
    val functions: List<ConcatenationFunction>,
    val xAxis: AbstractAxis,
    val yAxis: AbstractAxis,
    val settings: CanvasSettings
)