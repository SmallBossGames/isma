package ru.nstu.grin.concatenation.axis.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.text.Font
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.concatenation.axis.controller.NumberFormatter
import ru.nstu.grin.concatenation.axis.model.AxisSettings
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.canvas.model.CartesianSpace
import ru.nstu.grin.concatenation.axis.model.Direction
import java.lang.IllegalArgumentException
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.pow

class VerticalAxisDrawStrategy(
    private val axisSettings: AxisSettings,
    private val cartesianSpaces: List<CartesianSpace>
) : AxisMarksDrawStrategy {
    private val numberFormatter = NumberFormatter()

    override fun drawMarks(
        context: GraphicsContext,
        axis: ConcatenationAxis,
        marksCoordinate: Double
    ) {
        context.font = Font.font(axis.font, axis.textSize)
        println("Current step ${axisSettings.step}")
        val minYPixel = getTopAxisSize() * SettingsProvider.getAxisWidth()
        val maxYPixel = SettingsProvider.getCanvasHeight() - getBottomAxisSize() * SettingsProvider.getAxisWidth()

        val pixelLength = maxYPixel - minYPixel
        val unitLength = axis.settings.max - axisSettings.min
        val priceUnit = pixelLength / unitLength

        var currentY = maxYPixel
        val zeroPixel = axis.settings.min.absoluteValue * priceUnit + minYPixel

        while (currentY > minYPixel) {
            val stepY = (axis.settings.min + (maxYPixel - currentY) / priceUnit)
            val transformed = transformStepLogarithm(stepY, axisSettings.isLogarithmic)

            if (axis.settings.max > 0 && axis.settings.min < 0) {
                if ((currentY - zeroPixel).absoluteValue < (axis.distanceBetweenMarks/2)) {
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

    private fun getBottomAxisSize(): Int {
        return cartesianSpaces.filter {
            it.xAxis.direction == Direction.BOTTOM
                || it.yAxis.direction == Direction.BOTTOM
        }
            .size
    }

    private fun getTopAxisSize(): Int {
        return cartesianSpaces.filter { it.xAxis.direction == Direction.TOP || it.yAxis.direction == Direction.TOP }
            .size
    }

    private fun transformStepLogarithm(step: Double, isLogarithmic: Boolean): Double {
        return if (isLogarithmic) {
            axisSettings.logarithmBase.pow(step)
        } else {
            step
        }
    }

    private companion object {
        const val MAX_TEXT_WIDTH = 30.0
    }
}