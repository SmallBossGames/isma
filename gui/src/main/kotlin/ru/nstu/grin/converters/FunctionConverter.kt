package ru.nstu.grin.converters

import ru.nstu.grin.dto.FunctionDTO
import ru.nstu.grin.model.Axis
import ru.nstu.grin.model.Function

object FunctionConverter {
    fun merge(source: FunctionDTO, minDelta: Double, delta: Double): Function {
        return Function(
            pointArray = source.points,
            xAxis = Axis(
                delta = delta,
                minDelta = minDelta,
                deltas = listOf("Text"),
                position = source.xDirection,
                backGroundColor = source.xAxisColor,
                delimiterColor = source.xDelimeterColor,
                canvasHeight =,
                canvasWidth =
            ),
            yAxis = Axis(
                delta = delta,
                minDelta = minDelta,
                deltas = listOf(""),
                position = source.yDirection,
                backGroundColor = source.yAxisColor,
                delimiterColor = source.yDelimeterColor,
                canvasHeight =,
                canvasWidth =,
                ),
            functionColor = source.functionColor
        )
    }
}