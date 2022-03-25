package ru.nstu.grin.concatenation.cartesian.model

import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import java.util.*

data class CartesianSpace(
    var name: String,
    val functions: MutableList<ConcatenationFunction>,
    val xAxis: ConcatenationAxis,
    val yAxis: ConcatenationAxis,
    var isShowGrid: Boolean = false
) : Cloneable {

    public override fun clone(): CartesianSpace {
        return CartesianSpace(
            name = name,
            functions = functions
                .map { it.copy(id = UUID.randomUUID(), points = it.points.map { it.copy() }) }
                .toMutableList(),
            xAxis = xAxis.copy(settings = xAxis.settings.copy()),
            yAxis = yAxis.copy(settings = yAxis.settings.copy()),
            isShowGrid = isShowGrid
        )
    }

    fun merge(inFunctions: List<ConcatenationFunction>) {
        functions.addAll(inFunctions)
    }
}