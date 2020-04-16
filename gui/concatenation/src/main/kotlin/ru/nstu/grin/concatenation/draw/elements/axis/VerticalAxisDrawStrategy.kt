package ru.nstu.grin.concatenation.draw.elements.axis

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.concatenation.marks.MarksProvider
import ru.nstu.grin.concatenation.model.CanvasSettings
import ru.nstu.grin.concatenation.model.CartesianSpace
import ru.nstu.grin.concatenation.model.Direction

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
        println("Current step ${canvasSettings.step}")
        var drawStepY = "0.0"
        var currentStepY = 0.0
        var currentY = zeroPoint
        val minY = getTopAxisSize() * SettingsProvider.getAxisWidth()
        while (currentY > minY) {
            context.strokeText(
                drawStepY,
                marksCoordinate,
                currentY
            )

            currentY -= SettingsProvider.getMarksInterval()
            drawStepY = marksProvider.getNextMark(currentY, zeroPoint, currentStepY, canvasSettings.step)
            currentStepY += canvasSettings.step
        }

        drawStepY = "0.0"
        currentStepY = 0.0
        currentY = zeroPoint
        val maxY = SettingsProvider.getCanvasHeight() - getBottomAxisSize() * SettingsProvider.getAxisWidth()
        while (currentY < maxY) {
            if (currentStepY != 0.0)
                context.strokeText(
                    drawStepY,
                    marksCoordinate - 5,
                    currentY
                )

            currentY += SettingsProvider.getMarksInterval()
            drawStepY = marksProvider.getNextMark(currentY, zeroPoint, currentStepY, canvasSettings.step)
            currentStepY -= canvasSettings.step
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
}