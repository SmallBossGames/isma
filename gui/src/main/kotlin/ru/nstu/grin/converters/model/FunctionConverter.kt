package ru.nstu.grin.converters.model

import ru.nstu.grin.dto.FunctionDTO
import ru.nstu.grin.model.drawable.Function

object FunctionConverter {
    fun merge(source: FunctionDTO, minDelta: Double): Function {
        return Function(
            pointArray = source.points,
            xAxis = AxisConverter.merge(source.xAxis, minDelta),
            yAxis = AxisConverter.merge(source.yAxis, minDelta),
            functionColor = source.functionColor
        )
    }
}