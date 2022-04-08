package ru.nstu.grin.concatenation.axis.view

import javafx.geometry.VPos
import javafx.scene.canvas.GraphicsContext
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment
import ru.nstu.grin.concatenation.axis.controller.NumberFormatter
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.utilities.createStringValue
import ru.nstu.grin.concatenation.axis.utilities.estimateTextSize
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformer
import kotlin.math.absoluteValue
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

        context.stroke = axis.fontColor
        context.fill = axis.fontColor
        context.textAlign = TextAlignment.CENTER
        context.textBaseline = VPos.CENTER
        context.font = Font.font(axis.font, axis.textSize)

        // TODO: drawOnlyIntegerMark is temporary unavailable
        /*if (axis.settings.isOnlyIntegerPow) {
            drawOnlyIntegerMark(context, axis, marksCoordinate, zeroPixel)
        } else {
            drawDoubleMarks(context, axis, marksCoordinate, zeroPixel)
        }*/

        val (_, labelHeight) = estimateTextSize(axis.name, context.font)

        val marks = buildDoubleMarksArray(axis, context.font)
        val marksHeight = marks.maxOf { it.height }

        val offset = marksHeight - (labelHeight + marksHeight) / 2

        val marksY = marksCoordinate - marksHeight / 2 - DISTANCE_TO_LABEL / 2 + offset
        val labelY = marksCoordinate + DISTANCE_TO_LABEL / 2 + labelHeight / 2 + offset

        marks.forEach { context.fillText(it.text, it.x, marksY) }
        drawAxisLabel(context, axis, labelY)

        context.restore()
    }

    private data class Mark(val text: String, val x: Double, val height: Double, val width: Double)

    private fun buildDoubleMarksArray(axis: ConcatenationAxis, font: Font): List<Mark> {
        val result = mutableListOf<Mark>()

        val (minPixel, maxPixel) = matrixTransformer.getMinMaxPixel(axis.direction)

        val maxDrawingPixel = maxPixel - TEXT_VERTICAL_BORDERS_OFFSET
        val minDrawingPixel = minPixel + TEXT_VERTICAL_BORDERS_OFFSET

        val zeroPixel = matrixTransformer.transformUnitsToPixel(0.0, axis.settings, axis.direction)

        val zeroPixelOffset = if(zeroPixel < maxDrawingPixel && zeroPixel > minDrawingPixel){
            val text = createStringValue(0.0, axis)
            val (width, height) = estimateTextSize(text, font)

            if(zeroPixel + width / 2 < maxDrawingPixel && zeroPixel - width / 2 > minDrawingPixel){
                result.add(Mark(text, zeroPixel, height, width))
                width / 2
            } else{
                0.0
            }
        } else {
            0.0
        }

        var nextMarkPixel = max(zeroPixel + axis.distanceBetweenMarks, minDrawingPixel)
        var filledPosition = zeroPixel + zeroPixelOffset

        while (nextMarkPixel < maxDrawingPixel) {
            val currentValue = matrixTransformer
                .transformPixelToUnits(nextMarkPixel, axis.settings, axis.direction)

            val text = createStringValue(currentValue, axis)
            val (width, height) = estimateTextSize(text, font)

            if(nextMarkPixel - width / 2 - MIN_SPACE_BETWEEN_MARKS > filledPosition
                && nextMarkPixel + width / 2 < maxDrawingPixel
                && nextMarkPixel - width / 2 > minDrawingPixel
            ){
                filledPosition = nextMarkPixel + width / 2

                result.add(Mark(text, nextMarkPixel, height, width))
            }

            nextMarkPixel += axis.distanceBetweenMarks
        }

        nextMarkPixel = min(zeroPixel - axis.distanceBetweenMarks, maxDrawingPixel)
        filledPosition = zeroPixel - zeroPixelOffset

        while (nextMarkPixel > minDrawingPixel) {
            val currentValue = matrixTransformer
                .transformPixelToUnits(nextMarkPixel, axis.settings, axis.direction)

            val text = createStringValue(currentValue, axis)
            val (width, height) = estimateTextSize(text, font)

            if(nextMarkPixel + width / 2 + MIN_SPACE_BETWEEN_MARKS < filledPosition
                && nextMarkPixel + width / 2 < maxDrawingPixel
                && nextMarkPixel - width / 2 > minDrawingPixel
            ) {
                filledPosition = nextMarkPixel - width / 2

                result.add(Mark(text, nextMarkPixel, height, width))
            }

            nextMarkPixel -= axis.distanceBetweenMarks
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

    private fun drawOnlyIntegerMark(
        context: GraphicsContext,
        axis: ConcatenationAxis,
        marksCoordinate: Double,
        zeroPixel: Double
    ) {
        var currentX = axis.settings.min.toInt().toDouble()
        val max = axis.settings.max
        while (currentX < max) {
            val stepX = matrixTransformer
                .transformUnitsToPixel(currentX, axis.settings, axis.direction)

            if (axis.settings.max > 0 && axis.settings.min < 0) {
                if ((stepX - zeroPixel).absoluteValue < axis.distanceBetweenMarks) {
                    currentX += axis.settings.integerStep
                    continue
                }
            }

            val text = if (axis.isLogarithmic()) {
                NumberFormatter.formatLogarithmic(currentX, axis.settings.logarithmBase)
            } else {
                NumberFormatter.format(currentX)
            }

            context.fillText(
                text,
                stepX,
                marksCoordinate
            )
            currentX += axis.settings.integerStep
        }
    }

    private companion object {
        const val TEXT_VERTICAL_BORDERS_OFFSET = 5.0
        const val MIN_SPACE_BETWEEN_MARKS = 5.0
        const val DISTANCE_TO_LABEL = 2.0
    }
}