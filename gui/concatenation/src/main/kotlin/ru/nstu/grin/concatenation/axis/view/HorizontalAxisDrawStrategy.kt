package ru.nstu.grin.concatenation.axis.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.text.Font
import ru.nstu.grin.concatenation.axis.controller.NumberFormatter
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformerController
import tornadofx.Controller
import kotlin.math.absoluteValue
import kotlin.math.pow

class HorizontalAxisDrawStrategy : AxisMarksDrawStrategy, Controller() {
    private val numberFormatter = NumberFormatter()
    private val matrixTransformerController: MatrixTransformerController by inject()

    override fun drawMarks(
        context: GraphicsContext,
        axis: ConcatenationAxis,
        marksCoordinate: Double
    ) {
        context.font = Font.font(axis.font, axis.textSize)
        val (minPixel, maxPixel) = matrixTransformerController.getMinMaxPixel(axis.direction)

        val zeroPixel = matrixTransformerController.transformUnitsToPixel(0.0, axis.settings, axis.direction)

        var currentX = minPixel + 10.0
        while (currentX < maxPixel) {
            val stepX = matrixTransformerController.transformPixelToUnits(currentX, axis.settings, axis.direction)

            val transformed = transformStepLogarithm(stepX, axis.settings.isLogarithmic, axis.settings.logarithmBase)
            if (axis.settings.max > 0 && axis.settings.min < 0) {
                if ((currentX - zeroPixel).absoluteValue < axis.distanceBetweenMarks) {
                    currentX += axis.distanceBetweenMarks
                    continue
                }
            }
            context.strokeText(
                numberFormatter.format(transformed),
                currentX,
                marksCoordinate,
                MAX_TEXT_WIDTH
            )
            currentX += axis.distanceBetweenMarks
        }

        if (axis.settings.max > 0 && axis.settings.min < 0) {
            println("Draw zero")
            context.strokeText(
                numberFormatter.format(0.0),
                zeroPixel,
                marksCoordinate,
                MAX_TEXT_WIDTH
            )
        }
    }

    private fun transformStepLogarithm(step: Double, isLogarithmic: Boolean, logarithmBase: Double): Double {
        return if (isLogarithmic) {
            logarithmBase.pow(step)
        } else {
            step
        }
    }

    private companion object {
        const val MAX_TEXT_WIDTH = 30.0
    }
}