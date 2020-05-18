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
            val transformed = transformStepLogarithm(stepY, axis.settings.isLogarithmic, axis.settings.logarithmBase)

            if (axis.settings.max > 0 && axis.settings.min < 0) {
                if ((currentY - zeroPixel).absoluteValue < (axis.distanceBetweenMarks / 2)) {
                    println("Handled")
                    currentY -= axis.distanceBetweenMarks
                    continue
                }
            }

            context.strokeText(
                numberFormatter.format(transformed),
                marksCoordinate - 15.0,
                currentY,
                MAX_TEXT_WIDTH
            )

            currentY -= axis.distanceBetweenMarks
        }

        if (axis.settings.max > 0 && axis.settings.min < 0) {
            println("Draw zero")
            context.strokeText(
                numberFormatter.format(0.0),
                marksCoordinate - 15.0,
                zeroPixel,
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