package ru.nstu.grin.converters.model

import ru.nstu.grin.dto.concatenation.AxisDTO
import ru.nstu.grin.model.drawable.axis.TopAxis

object TopAxisConverter {
    fun merge(source: AxisDTO, minDelta: Double, startPoint: Double): TopAxis {
        return TopAxis(
            minDelta = minDelta,
            deltaMarks = source.deltaMarks,
            backGroundColor = source.color,
            delimiterColor = source.delimeterColor,
            startPoint = startPoint
        )
    }
}