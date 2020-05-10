package ru.nstu.grin.concatenation.canvas.model

import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction

data class CartesianSpace(
    val functions: MutableList<ConcatenationFunction>,
    val xAxis: ConcatenationAxis,
    val yAxis: ConcatenationAxis,
    var isShowGrid: Boolean = false
) : Cloneable {

    public override fun clone(): Any {
        return CartesianSpace(
            functions = functions.map { it.copy(points = it.points.map { it.copy() }) }.toMutableList(),
            xAxis = xAxis.copy(settings = xAxis.settings.copy()),
            yAxis = yAxis.copy(settings = yAxis.settings.copy()),
            isShowGrid = isShowGrid
        )
    }

    fun merge(inFunctions: List<ConcatenationFunction>) {
        functions.addAll(inFunctions)
    }
}