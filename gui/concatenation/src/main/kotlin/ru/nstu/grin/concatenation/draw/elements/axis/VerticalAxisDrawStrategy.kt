package ru.nstu.grin.concatenation.draw.elements.axis

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.concatenation.marks.MarksProvider
import ru.nstu.grin.concatenation.model.CanvasSettings
import ru.nstu.grin.concatenation.model.CartesianSpace
import ru.nstu.grin.concatenation.model.Direction
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.pow

class VerticalAxisDrawStrategy(
    private val canvasSettings: CanvasSettings,
    private val cartesianSpaces: List<CartesianSpace>
) : AxisMarksDrawStrategy {
    override fun drawMarks(
        context: GraphicsContext,
        zeroPoint: Double,
        direction: Direction,
        marksProvider: MarksProvider,
        marksCoordinate: Double
    ) {
        println("Current step ${canvasSettings.xStep}")
        var drawStepY = "0.0"
        var currentStepY = 0.0
        var currentY = zeroPoint
        val minY = getTopAxisSize() * SettingsProvider.getAxisWidth()
        while (currentY > minY) {
            val transformed = transformStepLogarithm(currentStepY, canvasSettings.isYLogarithmic)
            context.strokeText(
                format(transformed),
                marksCoordinate - 15.0,
                currentY,
                MAX_TEXT_WIDTH
            )

            currentY -= SettingsProvider.getMarksInterval()
            drawStepY = marksProvider.getNextMark(currentY, zeroPoint, currentStepY, canvasSettings.xStep)
            currentStepY += canvasSettings.xStep
        }

        drawStepY = "0.0"
        currentStepY = 0.0
        currentY = zeroPoint
        val maxY = SettingsProvider.getCanvasHeight() - getBottomAxisSize() * SettingsProvider.getAxisWidth()
        while (currentY < maxY) {
            if (currentStepY != 0.0) {
                val transformed = transformStepLogarithm(currentStepY, canvasSettings.isYLogarithmic)
                context.strokeText(
                    format(transformed),
                    marksCoordinate - 17.0,
                    currentY,
                    MAX_TEXT_WIDTH
                )
            }

            currentY += SettingsProvider.getMarksInterval()
            drawStepY = marksProvider.getNextMark(currentY, zeroPoint, currentStepY, canvasSettings.xStep)
            currentStepY -= canvasSettings.xStep
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
            canvasSettings.logarithmBase.pow(step)
        } else {
            step
        }
    }

    private fun format(number: Double): String {
        val decimal = BigDecimal(number)
        val formatter = DecimalFormat("0.0E0")
        formatter.roundingMode = RoundingMode.HALF_DOWN
        formatter.minimumFractionDigits = 2
        return formatter.format(decimal)
    }

    private companion object {
        const val MAX_TEXT_WIDTH = 30.0
    }
}