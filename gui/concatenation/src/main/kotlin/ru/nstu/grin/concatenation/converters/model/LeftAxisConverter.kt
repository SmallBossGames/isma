package ru.nstu.grin.concatenation.converters.model

import ru.nstu.grin.concatenation.dto.AxisDTO
import ru.nstu.grin.concatenation.model.axis.LeftAxis

object LeftAxisConverter {
    fun merge(source: AxisDTO, minDelta: Double, startPoint: Double): LeftAxis {
        return LeftAxis(
            minDelta = minDelta,
            deltaMarks = source.deltaMarks,
            backGroundColor = source.color,
            delimiterColor = source.delimeterColor,
            startPoint = startPoint
        )
    }
}