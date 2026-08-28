package ru.isma.next.editor.blueprint.utilities

import ru.isma.next.editor.blueprint.constants.*
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

data class ArrowGeometry(
    val lineStartX: Double,
    val lineStartY: Double,
    val lineEndX: Double,
    val lineEndY: Double,
    val arrowheadTranslateX: Double,
    val arrowheadTranslateY: Double,
    val arrowheadRotation: Double,
    val labelTextTranslateX: Double,
    val labelTextTranslateY: Double
)

fun calculateArrowGeometry(
    startX: Double,
    startY: Double,
    endX: Double,
    endY: Double,
    layoutX: Double,
    layoutY: Double,
    lineOffset: Double = ARROW_LINE_OFFSET,
    textXOffset: Double = ARROW_TEXT_X_OFFSET,
    textYOffset: Double = ARROW_TEXT_Y_OFFSET
): ArrowGeometry {
    val dx = endX - startX
    val dy = endY - startY
    val angle = atan2(dx, dy) + PI / 2

    val offsetX = lineOffset * sin(angle)
    val offsetY = lineOffset * cos(angle)

    return ArrowGeometry(
        lineStartX = startX - layoutX + offsetX,
        lineStartY = startY - layoutY + offsetY,
        lineEndX = endX - layoutX + offsetX,
        lineEndY = endY - layoutY + offsetY,
        arrowheadTranslateX = offsetX,
        arrowheadTranslateY = offsetY,
        arrowheadRotation = -angle / PI * 180.0,
        labelTextTranslateX = textXOffset * sin(angle),
        labelTextTranslateY = textYOffset * cos(angle)
    )
}
