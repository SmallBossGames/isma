package ru.nstu.grin.concatenation.axis.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.text.Font
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.concatenation.axis.controller.NumberFormatter
import ru.nstu.grin.concatenation.axis.marks.MarksProvider
import ru.nstu.grin.concatenation.axis.model.AxisSettings
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.canvas.model.CartesianSpace
import ru.nstu.grin.concatenation.axis.model.Direction
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
        val zeroPoint = axis.zeroPoint + axis.settings.correlation
        var currentStepY = 0.0
        var currentY = zeroPoint
        val minY = getTopAxisSize() * SettingsProvider.getAxisWidth()
        while (currentY > minY) {
            val stepY = -(currentY - zeroPoint) / axisSettings.pixelCost * axisSettings.step
            println(stepY)
            val transformed = transformStepLogarithm(stepY, axisSettings.isLogarithmic)
            context.strokeText(
                numberFormatter.format(transformed),
                marksCoordinate - 15.0,
                currentY,
                MAX_TEXT_WIDTH
            )

            currentY -= axis.distanceBetweenMarks
            currentStepY += axisSettings.step
        }

        currentStepY = 0.0
        currentY = zeroPoint
        val maxY = SettingsProvider.getCanvasHeight() - getBottomAxisSize() * SettingsProvider.getAxisWidth()
        while (currentY < maxY) {
            val stepY = -(currentY - zeroPoint) / axisSettings.pixelCost * axisSettings.step
            if (stepY != 0.0) {
                val transformed = transformStepLogarithm(stepY, axisSettings.isLogarithmic)
                context.strokeText(
                    numberFormatter.format(transformed),
                    marksCoordinate - 17.0,
                    currentY,
                    MAX_TEXT_WIDTH
                )
            }

            currentY += axis.distanceBetweenMarks
            currentStepY -= axisSettings.step
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