package ru.nstu.grin.concatenation.axis.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.text.Font
import ru.nstu.grin.concatenation.axis.controller.NumberFormatter
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformerController
import tornadofx.Controller
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

class VerticalAxisDrawStrategy : AxisMarksDrawStrategy, Controller() {
    private val numberFormatter = NumberFormatter()
    private val matrixTransformerController: MatrixTransformerController by inject()

    override fun drawMarks(
        context: GraphicsContext,
        axis: ConcatenationAxis,
        marksCoordinate: Double
    ) {
        context.font = Font.font(axis.font, axis.textSize)
        val zeroPixel = matrixTransformerController.transformUnitsToPixel(0.0, axis.settings, axis.direction)

        if (axis.settings.isOnlyIntegerPow) {
            println("IsOnlyIntger")
            drawOnlyIntegerMark(context, axis, marksCoordinate, zeroPixel)
        } else {
            drawDoubleMarks(context, axis, marksCoordinate, zeroPixel)
        }

        if (axis.settings.max > 0 && axis.settings.min < 0) {
            val zeroText = if (axis.isLogarithmic()) {
                numberFormatter.formatLogarithmic(0.0, axis.settings.logarithmBase)
            } else {
                numberFormatter.format(0.0)
            }
            context.strokeText(
                zeroText,
                marksCoordinate - 15.0,
                zeroPixel,
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

        var currentY = maxPixel - 10.0
        while (currentY > minPixel) {
            val stepY = matrixTransformerController.transformPixelToUnits(currentY, axis.settings, axis.direction)

            if (axis.settings.max > 0 && axis.settings.min < 0) {
                if ((currentY - zeroPixel).absoluteValue < (axis.distanceBetweenMarks / 2)) {
                    println("Handled")
                    currentY -= axis.distanceBetweenMarks
                    continue
                }
            }
            val text = if (axis.isLogarithmic()) {
                numberFormatter.formatLogarithmic(stepY, axis.settings.logarithmBase)
            } else {
                numberFormatter.format(stepY)
            }

            context.strokeText(
                text,
                marksCoordinate - 15.0,
                currentY,
                MAX_TEXT_WIDTH
            )

            currentY -= axis.distanceBetweenMarks
        }
    }

    private fun drawOnlyIntegerMark(
        context: GraphicsContext,
        axis: ConcatenationAxis,
        marksCoordinate: Double,
        zeroPixel: Double
    ) {
        var currentY = axis.settings.max.roundToInt().toDouble()
        val min = axis.settings.min
        while (currentY > min) {
            val stepY = matrixTransformerController.transformUnitsToPixel(currentY, axis.settings, axis.direction)

            if (axis.settings.max > 0 && axis.settings.min < 0) {
                if ((stepY - zeroPixel).absoluteValue < (axis.distanceBetweenMarks / 2)) {
                    println("Handled")
                    currentY -= axis.settings.integerStep
                    continue
                }
            }
            val text = if (axis.isLogarithmic()) {
                numberFormatter.formatLogarithmic(currentY, axis.settings.logarithmBase)
            } else {
                numberFormatter.format(currentY)
            }

            context.strokeText(
                text,
                marksCoordinate - 15.0,
                stepY,
                MAX_TEXT_WIDTH
            )

            currentY -= axis.settings.integerStep
        }
    }

    private companion object {
        const val MAX_TEXT_WIDTH = 30.0
    }
}