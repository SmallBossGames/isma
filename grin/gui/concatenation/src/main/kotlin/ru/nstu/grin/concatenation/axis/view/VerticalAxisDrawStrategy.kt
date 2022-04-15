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

class VerticalAxisDrawStrategy(
    private val matrixTransformer: MatrixTransformer
) : AxisMarksDrawStrategy {

    override fun drawMarks(
        context: GraphicsContext,
        axis: ConcatenationAxis,
        marksCoordinate: Double,
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
            println("IsOnlyIntger")
            drawOnlyIntegerMark(context, axis, marksCoordinate, zeroPixel)
        } else {
            drawDoubleMarks(context, axis, marksCoordinate, zeroPixel)
        }*/

        val (_, labelHeight) = estimateTextSize(axis.name, context.font)

        val marks = buildDoubleMarksArray(scaleProperties, styleProperties, axis.direction)
        val marksWidth = marks.maxOf { it.width }

        val offset = marksWidth - (labelHeight + marksWidth) / 2

        val marksX = marksCoordinate + marksWidth / 2 + DISTANCE_TO_LABEL / 2 - offset
        val labelX = marksCoordinate - labelHeight / 2 - DISTANCE_TO_LABEL / 2  - offset

        marks.forEach { context.fillText(it.text, marksX, it.y) }
        drawAxisLabel(context, axis, labelX)

        context.restore()
    }

    private data class Mark(val text: String, val y: Double, val height: Double, val width: Double)

    private fun buildDoubleMarksArray(
        scaleProperties: AxisScaleProperties,
        styleProperties: AxisStyleProperties,
        direction: Direction
    ): List<Mark> {
        val result = mutableListOf<Mark>()

        val (minPixel, maxPixel) = matrixTransformer.getMinMaxPixelVertical()

        val maxDrawingPixel = maxPixel - TEXT_VERTICAL_BORDERS_OFFSET
        val minDrawingPixel = minPixel + TEXT_VERTICAL_BORDERS_OFFSET

        val zeroPixel = matrixTransformer.transformUnitsToPixel(0.0, scaleProperties, direction)

        var maxWidth = 0.0

        val zeroPixelOffset = if(zeroPixel < maxDrawingPixel && zeroPixel > minDrawingPixel){
            val text = createStringValue(0.0, scaleProperties)
            val (width, height) = estimateTextSize(text, styleProperties.marksFont)

            maxWidth = max(maxWidth, width)

            if(zeroPixel + height / 2 < maxDrawingPixel && zeroPixel - height / 2 > minDrawingPixel){
                result.add(Mark(text, zeroPixel, height, width))
                height / 2
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

            maxWidth = max(maxWidth, width)

            if(nextMarkPixel - height / 2 - MIN_SPACE_BETWEEN_MARKS > filledPosition
                && nextMarkPixel + height / 2 < maxDrawingPixel
                && nextMarkPixel - height / 2 > minDrawingPixel
            ){
                filledPosition = nextMarkPixel + height / 2
                result.add(Mark(text, nextMarkPixel, height, width))
            }

            nextMarkPixel += styleProperties.marksDistance
        }

        nextMarkPixel = min(zeroPixel - zeroPixelOffset - styleProperties.marksDistance, maxDrawingPixel)
        filledPosition = zeroPixel - zeroPixelOffset

        while (nextMarkPixel > minDrawingPixel) {
            val currentValue = matrixTransformer
                .transformPixelToUnits(nextMarkPixel, scaleProperties, direction)

            val text = createStringValue(currentValue, scaleProperties)
            val (width, height) = estimateTextSize(text, styleProperties.marksFont)

            maxWidth = max(maxWidth, width)

            if(nextMarkPixel + height / 2 + MIN_SPACE_BETWEEN_MARKS < filledPosition
                && nextMarkPixel + height / 2 < maxDrawingPixel
                && nextMarkPixel - height / 2 > minDrawingPixel
            ) {
                filledPosition = nextMarkPixel - height / 2
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

        context.translate(labelCoordinate, minPixel + (maxPixel - minPixel) / 2)
        context.rotate(-90.0)
        context.fillText(axis.name, 0.0, 0.0)

        context.restore()
    }

    private companion object {
        const val TEXT_VERTICAL_BORDERS_OFFSET = 5.0
        const val MIN_SPACE_BETWEEN_MARKS = 5.0
        const val DISTANCE_TO_LABEL = 2.0
    }
}