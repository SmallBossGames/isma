package ru.nstu.grin.concatenation.draw.elements.axis

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.concatenation.marks.MarksProvider
import ru.nstu.grin.concatenation.model.AxisSettings
import ru.nstu.grin.concatenation.model.CartesianSpace
import ru.nstu.grin.concatenation.model.Direction
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.pow

class HorizontalAxisDrawStrategy(
    private val canvasSettings: AxisSettings,
    private val cartesianSpaces: List<CartesianSpace>
) : AxisMarksDrawStrategy {
    private val numberFormatter = NumberFormatter()

    override fun drawMarks(
        context: GraphicsContext,
        zeroPoint: Double,
        direction: Direction,
        marksProvider: MarksProvider,
        marksCoordinate: Double
    ) {
        println("CurrentStep x ${canvasSettings.step}")
        var drawStepX = "0.0"
        var currentStepX = 0.0
        var currentX = zeroPoint
        val minX = getLeftAxisSize() * SettingsProvider.getAxisWidth()
        while (currentX > minX) {
            val transformed = transformStepLogarithm(currentStepX, canvasSettings.isLogarithmic)
            context.strokeText(
                numberFormatter.format(transformed),
                currentX,
                marksCoordinate,
                MAX_TEXT_WIDTH
            )

            currentX -= SettingsProvider.getMarksInterval()
            drawStepX = marksProvider.getNextMark(currentX, zeroPoint, currentStepX, canvasSettings.step)
            currentStepX -= canvasSettings.step
        }

        drawStepX = "0.0"
        currentStepX = 0.0
        currentX = zeroPoint
        val maxX = SettingsProvider.getCanvasWidth() - getRightAxisSize() * SettingsProvider.getAxisWidth()
        while (currentX < maxX) {
            val transformed = transformStepLogarithm(currentStepX, canvasSettings.isLogarithmic)
            context.strokeText(
                numberFormatter.format(transformed),
                currentX,
                marksCoordinate,
                MAX_TEXT_WIDTH
            )

            currentX += SettingsProvider.getMarksInterval()
            drawStepX = marksProvider.getNextMark(currentX, zeroPoint, currentStepX, canvasSettings.step)
            currentStepX += canvasSettings.step
        }
    }

    private fun transformStepLogarithm(step: Double, isLogarithmic: Boolean): Double {
        return if (isLogarithmic) {
            canvasSettings.logarithmBase.pow(step)
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