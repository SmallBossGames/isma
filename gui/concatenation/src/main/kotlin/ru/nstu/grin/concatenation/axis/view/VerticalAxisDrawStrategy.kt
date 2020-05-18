package ru.nstu.grin.concatenation.axis.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.text.Font
import ru.nstu.grin.concatenation.axis.controller.NumberFormatter
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformerController
import tornadofx.Controller
import kotlin.math.absoluteValue
import kotlin.math.pow

class VerticalAxisDrawStrategy : AxisMarksDrawStrategy, Controller() {
    private val numberFormatter = NumberFormatter()
    private val matrixTransformerController: MatrixTransformerController by inject()

    override fun drawMarks(
        context: GraphicsContext,
        axis: ConcatenationAxis,
        marksCoordinate: Double
    ) {
        context.font = Font.font(axis.font, axis.textSize)
        println("Current step ${axis.settings.step}")
        val (minPixel, maxPixel) = matrixTransformerController.getMinMaxPixel(axis.direction)

        var currentY = maxPixel - 10.0
        val zeroPixel = matrixTransformerController.transformUnitsToPixel(0.0, axis.settings, axis.direction)

        while (currentY > minPixel) {
            val stepY = matrixTransformerController.transformPixelToUnits(currentY, axis.settings, axis.direction)

            if (axis.settings.max > 0 && axis.settings.min < 0) {
                if ((currentY - zeroPixel).absoluteValue < (axis.distanceBetweenMarks / 2)) {
                    println("Handled")
                    currentY -= axis.distanceBetweenMarks
                    continue
                }
            }
            val text = if (axis.settings.isLogarithmic) {
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

        if (axis.settings.max > 0 && axis.settings.min < 0) {
            println("Draw zero")
            val zeroText = if (axis.settings.isLogarithmic) {
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

    private companion object {
        const val MAX_TEXT_WIDTH = 30.0
    }
}