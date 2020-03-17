package ru.nstu.grin.concatenation.model

import ru.nstu.grin.concatenation.model.axis.ConcatenationAxis

data class CartesianSpace(
    val functions: MutableList<ConcatenationFunction>,
    val xAxis: ConcatenationAxis,
    val yAxis: ConcatenationAxis,
    val settings: CanvasSettings
) {
    fun merge(inFunctions: List<ConcatenationFunction>) {
        functions.addAll(inFunctions)
    }
}