package ru.nstu.grin.converters.model

import ru.nstu.grin.dto.AxisDTO
import ru.nstu.grin.model.drawable.axis.RightAxis

object RightAxisConverter {
    fun merge(source: AxisDTO, minDelta: Double, startPoint: Double): RightAxis {
        return RightAxis(
            minDelta = minDelta,
            deltaMarks = source.deltaMarks,
            position = source.direction,
            backGroundColor = source.color,
            delimiterColor = source.delimeterColor,
            startPoint = startPoint
        )
    }
}