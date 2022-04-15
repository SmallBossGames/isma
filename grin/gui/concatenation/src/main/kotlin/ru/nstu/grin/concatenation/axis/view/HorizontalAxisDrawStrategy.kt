package ru.nstu.grin.concatenation.axis.view

import javafx.geometry.VPos
import javafx.scene.canvas.GraphicsContext
import javafx.scene.text.TextAlignment
import ru.nstu.grin.concatenation.axis.model.AxisScaleProperties
import ru.nstu.grin.concatenation.axis.model.AxisStyleProperties
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.axis.utilities.createStringValue
import ru.nstu.grin.concatenation.axis.utilities.estimateTextSize
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformer
import kotlin.math.max
import kotlin.math.min

class HorizontalAxisDrawStrategy(
    private val matrixTransformer: MatrixTransformer
) : AxisMarksDrawStrategy {

    override fun drawMarks(
        context: GraphicsContext,
        axis: ConcatenationAxis,
        marksCoordinate: Double
    ) {
        context.save()

        val scaleProperties = axis.scaleProperties
        val styleProperties = axis.styleProperties

        context.stroke = styleProperties.marksColor
        context.fill = styleProperties.marksColor
        context.textAlign = TextAlignment.CENTER
        context.textBaseline = VPos.CENTER
        context.font = styleProperties.marksFont

        // TODO: drawOnlyIntegerMark is temporary unavailable
        /*if (axis.settings.isOnlyIntegerPow) {
            drawOnlyIntegerMark(context, axis, marksCoordinate, zeroPixel)
        } else {
            drawDoubleMarks(context, axis, marksCoordinate, zeroPixel)
        }*/

        val (_, labelHeight) = estimateTextSize(axis.name, context.font)

        val marks = buildDoubleMarksArray(scaleProperties, styleProperties, axis.direction)
        val marksHeight = marks.maxOf { it.height }

        val offset = marksHeight - (labelHeight + marksHeight) / 2

        val marksY = marksCoordinate - marksHeight / 2 - DISTANCE_TO_LABEL / 2 + offset
        val labelY = marksCoordinate + DISTANCE_TO_LABEL / 2 + labelHeight / 2 + offset

        marks.forEach { context.fillText(it.text, it.x, marksY) }
        drawAxisLabel(context, axis, labelY)

        context.restore()
    }

    private data class Mark(val text: String, val x: Double, val height: Double, val width: Double)

    private fun buildDoubleMarksArray(
        scaleProperties: AxisScaleProperties,
        styleProperties: AxisStyleProperties,
        direction: Direction
    ): List<Mark> {
        val result = mutableListOf<Mark>()

        val (minPixel, maxPixel) = matrixTransformer.getMinMaxPixelHorizontal()

        val maxDrawingPixel = maxPixel - TEXT_VERTICAL_BORDERS_OFFSET
        val minDrawingPixel = minPixel + TEXT_VERTICAL_BORDERS_OFFSET

        val zeroPixel = matrixTransformer.transformUnitsToPixel(0.0, scaleProperties, direction)

        val zeroPixelOffset = if(zeroPixel < maxDrawingPixel && zeroPixel > minDrawingPixel){
            val text = createStringValue(0.0, scaleProperties)
            val (width, height) = estimateTextSize(text, styleProperties.marksFont)

            if(zeroPixel + width / 2 < maxDrawingPixel && zeroPixel - width / 2 > minDrawingPixel){
                result.add(Mark(text, zeroPixel, height, width))
                width / 2
            } else{
                0.0
            }
        } else {
            0.0
        }

        var nextMarkPixel = max(zeroPixel + styleProperties.marksDistance, minDrawingPixel)
        var filledPosition = zeroPixel + zeroPixelOffset

        while (nextMarkPixel < maxDrawingPixel) {
            val currentValue = matrixTransformer
                .transformPixelToUnits(nextMarkPixel, scaleProperties, direction)

            val text = createStringValue(currentValue, scaleProperties)
            val (width, height) = estimateTextSize(text, styleProperties.marksFont)

            if(nextMarkPixel - width / 2 - MIN_SPACE_BETWEEN_MARKS > filledPosition
                && nextMarkPixel + width / 2 < maxDrawingPixel
                && nextMarkPixel - width / 2 > minDrawingPixel
            ){
                filledPosition = nextMarkPixel + width / 2

                result.add(Mark(text, nextMarkPixel, height, width))
            }

            nextMarkPixel += styleProperties.marksDistance
        }

        nextMarkPixel = min(zeroPixel - styleProperties.marksDistance, maxDrawingPixel)
        filledPosition = zeroPixel - zeroPixelOffset

        while (nextMarkPixel > minDrawingPixel) {
            val currentValue = matrixTransformer
                .transformPixelToUnits(nextMarkPixel, scaleProperties, direction)

            val text = createStringValue(currentValue, scaleProperties)
            val (width, height) = estimateTextSize(text, styleProperties.marksFont)

            if(nextMarkPixel + width / 2 + MIN_SPACE_BETWEEN_MARKS < filledPosition
                && nextMarkPixel + width / 2 < maxDrawingPixel
                && nextMarkPixel - width / 2 > minDrawingPixel
            ) {
                filledPosition = nextMarkPixel - width / 2

                result.add(Mark(text, nextMarkPixel, height, width))
            }

            nextMarkPixel -= styleProperties.marksDistance
        }

        return result
    }

    private fun drawAxisLabel(
        context: GraphicsContext,
        axis: ConcatenationAxis,
        labelCoordinate: Double,
    ){
        context.save()

        val (minPixel, maxPixel) = matrixTransformer.getMinMaxPixel(axis.direction)

        context.translate(minPixel + (maxPixel - minPixel) / 2, labelCoordinate)
        context.rotate(0.0)
        context.fillText(axis.name, 0.0, 0.0)

        context.restore()
    }

    private companion object {
        const val TEXT_VERTICAL_BORDERS_OFFSET = 5.0
        const val MIN_SPACE_BETWEEN_MARKS = 5.0
        const val DISTANCE_TO_LABEL = 2.0
    }
}