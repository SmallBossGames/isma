package ru.nstu.grin.converters.model

import ru.nstu.grin.dto.concatenation.AxisDTO
import ru.nstu.grin.model.Direction
import ru.nstu.grin.model.drawable.axis.AbstractAxis

object AxisConverter {
    fun merge(source: AxisDTO, minDelta: Double, startPoint: Double): AbstractAxis {
        return when (source.direction.direction) {
            Direction.LEFT -> LeftAxisConverter.merge(source, minDelta, startPoint)
            Direction.RIGHT -> RightAxisConverter.merge(source, minDelta, startPoint)
            Direction.TOP -> TopAxisConverter.merge(source, minDelta, startPoint)
            Direction.BOTTOM -> BottomAxisConverter.merge(source, minDelta, startPoint)
        }
    }
}