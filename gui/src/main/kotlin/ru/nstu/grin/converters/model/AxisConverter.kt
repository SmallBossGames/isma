package ru.nstu.grin.converters.model

import ru.nstu.grin.dto.AxisDTO
import ru.nstu.grin.model.drawable.Axis

object AxisConverter {
    fun merge(source: AxisDTO, minDelta: Double): Axis {
        return Axis(
            minDelta = minDelta,
            deltaMarks = source.deltaMarks,
            position = source.direction,
            backGroundColor = source.color,
            delimiterColor = source.delimeterColor
        )
    }
}