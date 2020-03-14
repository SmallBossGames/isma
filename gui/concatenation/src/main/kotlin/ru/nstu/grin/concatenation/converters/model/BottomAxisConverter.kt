package ru.nstu.grin.concatenation.converters.model

import ru.nstu.grin.concatenation.dto.AxisDTO
import ru.nstu.grin.concatenation.model.axis.BottomAxis

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