package ru.nstu.grin.concatenation.axis.converter

import javafx.scene.text.Font
import ru.nstu.grin.concatenation.axis.dto.ConcatenationAxisDTO
import ru.nstu.grin.concatenation.axis.model.AxisStyleProperties
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis

object ConcatenationAxisConverter {
    fun merge(source: ConcatenationAxisDTO, order: Int): ConcatenationAxis {
        return ConcatenationAxis(
            name = source.name,
            order = order,
            direction = source.direction,
            styleProperties = AxisStyleProperties(
                backgroundColor = source.backGroundColor,
                marksDistance = source.distanceBetweenMarks,
                marksFont = Font.font(source.font, source.textSize),
                marksColor = source.delimeterColor,
            )
        )
    }
}
