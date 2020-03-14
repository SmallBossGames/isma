package ru.nstu.grin.concatenation.converters.model

import ru.nstu.grin.concatenation.dto.AxisDTO
import ru.nstu.grin.concatenation.model.axis.TopAxis

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