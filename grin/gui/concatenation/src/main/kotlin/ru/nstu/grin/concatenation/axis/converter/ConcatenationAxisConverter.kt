package ru.nstu.grin.concatenation.axis.converter

import ru.nstu.grin.concatenation.axis.dto.ConcatenationAxisDTO
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis

object ConcatenationAxisConverter {
    fun merge(source: ConcatenationAxisDTO, order: Int): ConcatenationAxis {
        return ConcatenationAxis(
            id = source.id,
            name = source.name,
            zeroPoint = source.zeroPoint,
            order = order,
            direction = source.direction,
            backGroundColor = source.backGroundColor,
            fontColor = source.delimeterColor,
            distanceBetweenMarks = source.distanceBetweenMarks,
            textSize = source.textSize,
            font = source.font
        )
    }
}
