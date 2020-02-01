package ru.nstu.grin.converters.model

import ru.nstu.grin.dto.FunctionDTO
import ru.nstu.grin.model.drawable.Function
import ru.nstu.grin.model.drawable.axis.AbstractAxis

object FunctionConverter {
    fun merge(
        source: FunctionDTO,
        minDelta: Double,
        xStartPoint: Double,
        yStartPoint: Double,
        xAxises: List<Pair<String, AbstractAxis>>,
        yAxises: List<Pair<String, AbstractAxis>>
    ): Function {
        val xAxis = source.xAxis.direction.functionName?.let { name ->
            xAxises.filter { it.first == name }.first().second
        } ?: AxisConverter.merge(source.xAxis, minDelta, xStartPoint)
        val yAxis = source.yAxis.direction.functionName?.let { name ->
            yAxises.filter { it.first == name }.first().second
        } ?: AxisConverter.merge(source.yAxis, minDelta, yStartPoint)
        return Function(
            name = source.name,
            pointArray = source.points,
            xAxis = xAxis,
            yAxis = yAxis,
            functionColor = source.functionColor
        )
    }
}