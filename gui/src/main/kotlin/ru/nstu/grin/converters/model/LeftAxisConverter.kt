package ru.nstu.grin.converters.model

import ru.nstu.grin.dto.concatenation.AxisDTO
import ru.nstu.grin.model.drawable.axis.LeftAxis

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