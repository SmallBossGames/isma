package ru.nstu.grin.concatenation.cartesian.model

import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import java.util.*

data class CartesianSpace(
    val id: UUID,
    var name: String,
    val functions: MutableList<ConcatenationFunction>,
    val xAxis: ConcatenationAxis,
    val yAxis: ConcatenationAxis,
    var isShowGrid: Boolean = false
) : Cloneable {

    public override fun clone(): CartesianSpace {
        return CartesianSpace(
            id = UUID.randomUUID(),
            name = name,
            functions = functions.map {
                it.copy(id = UUID.randomUUID(), points = it.points.map { it.copy() })
            }.toMutableList(),
            xAxis = xAxis.copy(id = UUID.randomUUID(), settings = xAxis.settings.copy()),
            yAxis = yAxis.copy(id = UUID.randomUUID(), settings = yAxis.settings.copy()),
            isShowGrid = isShowGrid
        )
    }

    fun merge(inFunctions: List<ConcatenationFunction>) {
        functions.addAll(inFunctions)
    }
}