package ru.nstu.grin.concatenation.converters.model

import ru.nstu.grin.concatenation.dto.FunctionDTO
import ru.nstu.grin.concatenation.model.ConcatenationFunction
import ru.nstu.grin.concatenation.model.axis.AbstractAxis

object ConcatenationFunctionConverter {
    fun merge(
        source: FunctionDTO,
        minDelta: Double,
        xStartPoint: Double,
        yStartPoint: Double,
        xAxises: List<Pair<String, AbstractAxis>>,
        yAxises: List<Pair<String, AbstractAxis>>
    ): ConcatenationFunction {
        val xAxis = source.xAxis.direction.functionName?.let { name ->
            xAxises.filter { it.first == name }.first().second
        } ?: AxisConverter.merge(source.xAxis, minDelta, xStartPoint)
        val yAxis = source.yAxis.direction.functionName?.let { name ->
            yAxises.filter { it.first == name }.first().second
        } ?: AxisConverter.merge(source.yAxis, minDelta, yStartPoint)
        return ConcatenationFunction(
            name = source.name,
            points = source.points,
            xAxis = xAxis,
            yAxis = yAxis,
            functionColor = source.functionColor
        )
    }
}