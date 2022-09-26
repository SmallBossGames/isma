package ru.nstu.grin.concatenation.cartesian.model

import ru.nstu.grin.concatenation.description.model.Description
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction

data class CartesianSpace(
    var name: String,
    val functions: MutableList<ConcatenationFunction>,
    val descriptions: MutableList<Description>,
    val xAxis: ConcatenationAxis,
    val yAxis: ConcatenationAxis,
    var isShowGrid: Boolean = false
) : Cloneable {

    val axes = listOf(xAxis, yAxis)

    public override fun clone(): CartesianSpace {
        return copy(
            functions = functions.map { it.copy() }.toMutableList(),
            xAxis = xAxis.copy(),
            yAxis = yAxis.copy(),
        )
    }

    fun merge(inFunctions: List<ConcatenationFunction>) {
        functions.addAll(inFunctions)
    }
}