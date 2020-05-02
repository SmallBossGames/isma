package ru.nstu.grin.concatenation.canvas.model

import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction

data class CartesianSpace(
    val functions: MutableList<ConcatenationFunction>,
    val xAxis: ConcatenationAxis,
    val yAxis: ConcatenationAxis,
    var isShowGrid: Boolean = false
) {
    fun merge(inFunctions: List<ConcatenationFunction>) {
        functions.addAll(inFunctions)
    }
}