package ru.nstu.grin.converters.model

import ru.nstu.grin.dto.AxisDTO
import ru.nstu.grin.model.drawable.axis.BottomAxis

object BottomAxisConverter {
    fun merge(source: AxisDTO, minDelta: Double, startPoint: Double): BottomAxis {
        return BottomAxis(
            minDelta = minDelta,
            deltaMarks = source.deltaMarks,
            backGroundColor = source.color,
            delimiterColor = source.delimeterColor,
            startPoint = startPoint
        )
    }
}