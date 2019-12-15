package ru.nstu.grin.model.drawable.axis

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.model.Direction
import ru.nstu.grin.settings.SettingProvider

class RightAxis(
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
        return x > (SettingProvider.getCanvasWidth() - Axis.WIDTH_AXIS - startPoint) &&
            x < (SettingProvider.getCanvasWidth() - Axis.WIDTH_AXIS)
    }

    override fun drawRectangle(graphicsContext: GraphicsContext) {
        graphicsContext.fill = backGroundColor
        graphicsContext.fillRect(
            SettingProvider.getCanvasWidth() - Axis.WIDTH_AXIS - startPoint,
            0.0,
            Axis.WIDTH_AXIS,
            SettingProvider.getCanvasHeight()
        )
    }

    override fun drawMinorDelimiters(graphicsContext: GraphicsContext) {
        graphicsContext.stroke = delimiterColor
        var current = 0.0
        while (current < SettingProvider.getCanvasHeight() - WIDTH_AXIS) {
            graphicsContext.strokeLine(SettingProvider.getCanvasHeight() - startPoint - WIDTH_AXIS - WIDTH_DELIMITER, current,
                SettingProvider.getCanvasHeight() - startPoint - WIDTH_AXIS, current)
            current += minDelta
        }
    }

    override fun drawDeltaMarks(graphicsContext: GraphicsContext) {
        val normalMarks = deltaMarks.reversed()
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}