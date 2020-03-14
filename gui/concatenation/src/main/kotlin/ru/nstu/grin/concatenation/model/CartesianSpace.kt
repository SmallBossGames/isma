package ru.nstu.grin.concatenation.model

import ru.nstu.grin.concatenation.model.axis.AbstractAxis

data class CartesianSpace(
    val functions: List<ConcatenationFunction>,
    val xAxis: AbstractAxis,
    val yAxis: AbstractAxis,
    val settings: CanvasSettings
)