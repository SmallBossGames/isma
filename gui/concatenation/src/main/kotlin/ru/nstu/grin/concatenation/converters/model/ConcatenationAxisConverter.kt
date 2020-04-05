package ru.nstu.grin.concatenation.converters.model

import ru.nstu.grin.concatenation.dto.ConcatenationAxisDTO
import ru.nstu.grin.concatenation.marks.DoubleMarksProvider
import ru.nstu.grin.concatenation.marks.InvertDoubleMarksProvider
import ru.nstu.grin.concatenation.model.Direction
import ru.nstu.grin.concatenation.model.axis.ConcatenationAxis

object ConcatenationAxisConverter {
    fun merge(source: ConcatenationAxisDTO, order: Int): ConcatenationAxis {
        return ConcatenationAxis(
            name = source.name,
            zeroPoint = source.zeroPoint,
            marksProvider = when (source.direction.direction) {
                Direction.LEFT, Direction.RIGHT -> {
                    InvertDoubleMarksProvider()
                }
                Direction.TOP, Direction.BOTTOM -> {
                    DoubleMarksProvider()
                }
            },
            order = order,
            direction = source.direction.direction,
            backGroundColor = source.backGroundColor,
            delimiterColor = source.delimeterColor
        )
    }
}
