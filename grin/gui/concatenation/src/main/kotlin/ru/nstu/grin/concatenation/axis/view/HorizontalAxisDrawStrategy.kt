package ru.nstu.grin.concatenation.axis.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.text.Font
import ru.nstu.grin.concatenation.axis.controller.NumberFormatter
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformerController
import tornadofx.Controller
import kotlin.math.absoluteValue

class HorizontalAxisDrawStrategy : AxisMarksDrawStrategy, Controller() {
    private val matrixTransformerController: MatrixTransformerController by inject()

    override fun drawMarks(
        context: GraphicsContext,
        axis: ConcatenationAxis,
        marksCoordinate: Double
    ) {
        context.font = Font.font(axis.font, axis.textSize)
        val zeroPixel = matrixTransformerController.transformUnitsToPixel(0.0, axis.settings, axis.direction)

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

    private fun drawDoubleMarks(
        context: GraphicsContext,
        axis: ConcatenationAxis,
        marksCoordinate: Double,
        zeroPixel: Double
    ) {
        val (minPixel, maxPixel) = matrixTransformerController.getMinMaxPixel(axis.direction)

        var currentX = minPixel + 10.0
        while (currentX < maxPixel) {
            val stepX = matrixTransformerController.transformPixelToUnits(currentX, axis.settings, axis.direction)

            if (axis.settings.max > 0 && axis.settings.min < 0) {
                if ((currentX - zeroPixel).absoluteValue < axis.distanceBetweenMarks) {
                    currentX += axis.distanceBetweenMarks
                    continue
                }
            }
            val text = if (axis.isLogarithmic()) {
                NumberFormatter.formatLogarithmic(stepX, axis.settings.logarithmBase)
            } else {
                NumberFormatter.format(stepX)
            }
            context.strokeText(
                text,
                currentX,
                marksCoordinate,
                MAX_TEXT_WIDTH
            )
            currentX += axis.distanceBetweenMarks
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
            val stepX = matrixTransformerController.transformUnitsToPixel(currentX, axis.settings, axis.direction)

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
    }
}