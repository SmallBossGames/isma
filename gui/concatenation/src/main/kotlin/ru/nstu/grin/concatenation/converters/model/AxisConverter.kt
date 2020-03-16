package ru.nstu.grin.concatenation.converters.model

import ru.nstu.grin.concatenation.dto.AxisDTO
import ru.nstu.grin.concatenation.marks.DoubleMarksProvider
import ru.nstu.grin.concatenation.model.axis.ConcatenationAxis

object AxisConverter {
    fun merge(source: AxisDTO, order: Int, zeroPoint: Double): ConcatenationAxis {
        return ConcatenationAxis(
            zeroPoint = zeroPoint,
            marksProvider = DoubleMarksProvider(),
            order = order,
            direction = source.direction.direction,
            backGroundColor = source.backGroundColor,
            delimiterColor = source.delimeterColor
        )
    }
}