package ru.nstu.grin.concatenation.axis.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.text.Font
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.concatenation.axis.model.AxisSettings
import ru.nstu.grin.concatenation.canvas.model.CartesianSpace
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.axis.controller.NumberFormatter
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import kotlin.contracts.contract
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.pow

class HorizontalAxisDrawStrategy(
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
        val minPixelX = getLeftAxisSize() * SettingsProvider.getAxisWidth()
        val maxPixelX = SettingsProvider.getCanvasWidth() - getRightAxisSize() * SettingsProvider.getAxisWidth()

        val sum = axis.settings.max + abs(axisSettings.min)
        val sumPixel = maxPixelX - minPixelX
        val price = sumPixel / sum
        val zeroPixel = axis.settings.min.absoluteValue * price + minPixelX

        var currentX = minPixelX
        while (currentX < maxPixelX) {
            val stepX = axis.settings.min + (currentX - minPixelX) / price

            val transformed = transformStepLogarithm(stepX, axisSettings.isLogarithmic)
            if (axis.settings.max > 0 && axis.settings.min < 0) {
                if ((currentX-zeroPixel).absoluteValue<axis.distanceBetweenMarks) {
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

    private fun transformStepLogarithm(step: Double, isLogarithmic: Boolean): Double {
        return if (isLogarithmic) {
            axisSettings.logarithmBase.pow(step)
        } else {
            step
        }
    }

    private fun getLeftAxisSize(): Int {
        return cartesianSpaces.filter { it.xAxis.direction == Direction.LEFT || it.yAxis.direction == Direction.LEFT }
            .size
    }

    private fun getRightAxisSize(): Int {
        return cartesianSpaces.filter { it.xAxis.direction == Direction.RIGHT || it.yAxis.direction == Direction.RIGHT }
            .size
    }

    private companion object {
        const val MAX_TEXT_WIDTH = 30.0
    }
}