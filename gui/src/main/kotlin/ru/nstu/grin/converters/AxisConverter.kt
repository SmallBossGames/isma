package ru.nstu.grin.converters

import ru.nstu.grin.dto.AxisDTO
import ru.nstu.grin.model.drawable.Axis

object AxisConverter {
    fun merge(source: AxisDTO, minDelta: Double, delta: Double): Axis {
        return Axis(
            delta = delta,
            minDelta = minDelta,
            deltas = listOf("Test"),
            position = source.direction,
            backGroundColor = source.color,
            delimiterColor = source.delimeterColor
        )
    }
}