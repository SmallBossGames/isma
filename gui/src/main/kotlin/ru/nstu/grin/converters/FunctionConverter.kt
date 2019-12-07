package ru.nstu.grin.converters

import ru.nstu.grin.dto.FunctionDTO
import ru.nstu.grin.model.drawable.Function

object FunctionConverter {
    fun merge(source: FunctionDTO, minDelta: Double, delta: Double): Function {
        return Function(
            pointArray = source.points,
            xAxis = AxisConverter.merge(source.xAxis, minDelta, delta),
            yAxis = AxisConverter.merge(source.yAxis, minDelta, delta),
            functionColor = source.functionColor
        )
    }
}