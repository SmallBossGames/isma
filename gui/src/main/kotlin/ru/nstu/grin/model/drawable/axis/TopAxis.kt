package ru.nstu.grin.model.drawable.axis

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.model.Direction
import ru.nstu.grin.settings.SettingProvider

class TopAxis(
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
        return y < Axis.WIDTH_AXIS + startPoint && y > startPoint
    }

    override fun drawRectangle(graphicsContext: GraphicsContext) {
        graphicsContext.fill = backGroundColor
        graphicsContext.fillRect(0.0, startPoint, SettingProvider.getCanvasWidth(), Axis.WIDTH_AXIS)
    }

    override fun drawMinorDelimiters(graphicsContext: GraphicsContext) {
        graphicsContext.stroke = delimiterColor
        var current = 0.0
        while (current < SettingProvider.getCanvasWidth()) {
            graphicsContext.strokeLine(
                current,
                startPoint + WIDTH_AXIS - WIDTH_DELIMITER,
                current,
                startPoint + WIDTH_AXIS
            )
            current += minDelta
        }
    }

    override fun drawDeltaMarks(graphicsContext: GraphicsContext) {
        var current = 0.0
        var i = 0
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}