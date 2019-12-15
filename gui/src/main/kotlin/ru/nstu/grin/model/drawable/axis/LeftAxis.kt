package ru.nstu.grin.model.drawable.axis

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.model.Direction
import ru.nstu.grin.settings.SettingProvider

class LeftAxis(
    private val startPoint: Double,
    private val minDelta: Double,
    private val deltaMarks: List<Double>,
    position: Direction,
    private val backGroundColor: Color,
    private val delimiterColor: Color
) : AbstractAxis(
    startPoint, minDelta, deltaMarks, position, backGroundColor, delimiterColor
) {
    override fun isOnIt(x: Double, y: Double): Boolean {
        return x < Axis.WIDTH_AXIS + startPoint && x > startPoint
    }

    override fun drawRectangle(graphicsContext: GraphicsContext) {
        graphicsContext.fill = backGroundColor
        graphicsContext.fillRect(startPoint, 0.0, Axis.WIDTH_AXIS, SettingProvider.getCanvasHeight())
    }

    override fun drawMinorDelimiters(graphicsContext: GraphicsContext) {
        graphicsContext.stroke = delimiterColor
        var current = 0.0
        while (current < SettingProvider.getCanvasHeight() - WIDTH_AXIS) {
            graphicsContext.strokeLine(startPoint + WIDTH_AXIS - WIDTH_DELIMITER, current,
                startPoint + WIDTH_AXIS, current)
            current += minDelta
        }
    }

    override fun drawDeltaMarks(graphicsContext: GraphicsContext) {
        val normalMarks = deltaMarks.reversed()

        var current = 0.0
        var i = 0
        while (current < SettingProvider.getCanvasWidth() && i < normalMarks.size) {
            graphicsContext.strokeText(
                normalMarks[i].toString(), Axis.WIDTH_AXIS - Axis.TEXT_ALIGN,
                current - Axis.WIDTH_AXIS
            )
            i++
            current += minDelta * DEFAULT_DELTA_SPACE
        }
    }
}