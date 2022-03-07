package ru.nstu.grin.concatenation.axis.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.text.Font
import ru.nstu.grin.concatenation.axis.controller.NumberFormatter
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
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
        context.font = Font.font(axis.font, axis.textSize)

        val zeroPixel = matrixTransformerController
            .transformUnitsToPixel(0.0, axis.settings, axis.direction)

        if (axis.settings.isOnlyIntegerPow) {
            drawOnlyIntegerMark(context, axis, marksCoordinate, zeroPixel)
        } else {
            drawDoubleMarks(context, axis, marksCoordinate, zeroPixel)
        }

        if (axis.settings.max > 0 && axis.settings.min < 0) {
            val zeroText = if (axis.isLogarithmic()) {
                NumberFormatter.formatLogarithmic(0.0, axis.settings.logarithmBase)
            } else {
                NumberFormatter.format(0.0)
            }
            context.strokeText(
                zeroText,
                zeroPixel,
                marksCoordinate,
                MAX_TEXT_WIDTH
            )
        }
    }

    private fun drawDoubleMark(
        context: GraphicsContext,
        axis: ConcatenationAxis,
        marksCoordinate: Double,
        value: Double,
        pixel: Double
    ){
        val text = if (axis.isLogarithmic()) {
            NumberFormatter.formatLogarithmic(value, axis.settings.logarithmBase)
        } else {
            NumberFormatter.format(value)
        }

        context.strokeText(
            text,
            pixel,
            marksCoordinate,
            MAX_TEXT_WIDTH
        )
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

        if(zeroPixel < maxDrawingPixel && zeroPixel > minDrawingPixel){
            drawDoubleMark(context, axis, marksCoordinate, 0.0, zeroPixel)
        }

        var currentPixel = max(zeroPixel + axis.distanceBetweenMarks, minDrawingPixel)

        while (currentPixel < maxDrawingPixel) {
            val currentValue = matrixTransformerController
                .transformPixelToUnits(currentPixel, axis.settings, axis.direction)

            drawDoubleMark(context, axis, marksCoordinate, currentValue, currentPixel)

            currentPixel += axis.distanceBetweenMarks
        }

        currentPixel = min(zeroPixel - axis.distanceBetweenMarks, maxDrawingPixel)

        while (currentPixel > minDrawingPixel) {
            val currentValue = matrixTransformerController
                .transformPixelToUnits(currentPixel, axis.settings, axis.direction)

            drawDoubleMark(context, axis, marksCoordinate, currentValue, currentPixel)

            currentPixel -= axis.distanceBetweenMarks
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

            context.strokeText(
                text,
                stepX,
                marksCoordinate,
                MAX_TEXT_WIDTH
            )
            currentX += axis.settings.integerStep
        }
    }

    private companion object {
        const val MAX_TEXT_WIDTH = 30.0
        const val TEXT_VERTICAL_BORDERS_OFFSET = 10.0
    }
}