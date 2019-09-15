package ru.nstu.grin.converters

import ru.nstu.grin.dto.FunctionDTO
import ru.nstu.grin.model.Function

object FunctionConverter {
    fun merge(source: FunctionDTO, minDelta: Double, delta: Double): Function {
        return Function(
            pointArray = source.points,
            xDirection = source.xDirection,
            yDirection = source.yDirection,
            minDelta = minDelta,
            delta = delta,
            functionColor = source.functionColor,
            xAxisColor = source.xAxisColor,
            yAxisColor = source.yAxisColor
        )
    }
}