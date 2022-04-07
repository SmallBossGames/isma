package ru.nstu.grin.concatenation.axis.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment
import ru.nstu.grin.concatenation.axis.controller.NumberFormatter
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.utilities.createStringValue
import ru.nstu.grin.concatenation.axis.utilities.estimateTextSize
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformerController
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

class HorizontalAxisDrawStrategy(
    private val matrixTransformerController: MatrixTransformerController
) : AxisMarksDrawStrategy {

    override fun drawMarks(
        context: GraphicsContext,
        axis: ConcatenationAxis,
        marksCoordinate: Double,
        canvasWidth: Double,
        canvasHeight: Double
    ) {
        context.stroke = axis.fontColor
        context.fill = axis.fontColor
        context.textAlign = TextAlignment.CENTER
        context.font = Font.font(axis.font, axis.textSize)

        val zeroPixel = matrixTransformerController
            .transformUnitsToPixel(0.0, axis.settings, axis.direction)

        if (axis.settings.isOnlyIntegerPow) {
            drawOnlyIntegerMark(context, axis, marksCoordinate, zeroPixel)
        } else {
            drawDoubleMarks(context, axis, marksCoordinate, zeroPixel)
        }
    }

    private fun drawDoubleMarks(
        context: GraphicsContext,
        axis: ConcatenationAxis,
        marksCoordinate: Double,
        zeroPixel: Double
    ) {
        val (minPixel, maxPixel) = matrixTransformerController.getMinMaxPixel(axis.direction)

        val maxDrawingPixel = maxPixel - TEXT_VERTICAL_BORDERS_OFFSET
        val minDrawingPixel = minPixel + TEXT_VERTICAL_BORDERS_OFFSET

        val zeroPixelOffset = if(zeroPixel < maxDrawingPixel && zeroPixel > minDrawingPixel){
            val text = createStringValue(0.0, axis)
            val (width, height) = estimateTextSize(text, context.font)

            if(zeroPixel + width / 2 < maxDrawingPixel && zeroPixel - width / 2 > minDrawingPixel){
                context.fillText(text, zeroPixel, marksCoordinate)
                width / 2
            } else{
                0.0
            }
        } else {
            0.0
        }

        var currentPixel = max(zeroPixel + zeroPixelOffset + axis.distanceBetweenMarks, minDrawingPixel)

        while (currentPixel < maxDrawingPixel) {
            val currentValue = matrixTransformerController
                .transformPixelToUnits(currentPixel, axis.settings, axis.direction)

            val text = createStringValue(currentValue, axis)
            val (width, height) = estimateTextSize(text, context.font)

            if(currentPixel + width < maxDrawingPixel){
                context.fillText(text, currentPixel + width / 2, marksCoordinate)
            }

            currentPixel += axis.distanceBetweenMarks + width
        }

        currentPixel = min(zeroPixel - zeroPixelOffset - axis.distanceBetweenMarks, maxDrawingPixel)

        while (currentPixel > minDrawingPixel) {
            val currentValue = matrixTransformerController
                .transformPixelToUnits(currentPixel, axis.settings, axis.direction)

            val text = createStringValue(currentValue, axis)
            val (width, height) = estimateTextSize(text, context.font)

            println(width)

            if(currentPixel - width > minDrawingPixel){
                context.fillText(text, currentPixel - width / 2, marksCoordinate)
            }

            currentPixel -= axis.distanceBetweenMarks + width
        }
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
            val stepX = matrixTransformerController
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
    }
}