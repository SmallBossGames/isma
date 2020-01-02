package ru.nstu.grin.model.drawable.axis

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.model.CoordinateDirection
import ru.nstu.grin.model.Direction
import ru.nstu.grin.model.Drawable
import ru.nstu.grin.settings.SettingProvider

class RightAxis(
    private val startPoint: Double,
    private val minDelta: Double,
    private val deltaMarks: List<Double>,
    private val backGroundColor: Color,
    private val delimiterColor: Color
) : AbstractAxis(
    startPoint, minDelta, deltaMarks, backGroundColor, delimiterColor
) {
    override fun scale(scale: Double, direction: CoordinateDirection): Drawable {
        val newDeltas = deltaMarks.map { it * scale }
        return RightAxis(
            startPoint, minDelta, newDeltas, backGroundColor, delimiterColor
        )
    }

    override fun isOnIt(x: Double, y: Double): Boolean {
        return x > (SettingProvider.getCanvasWidth() - WIDTH_AXIS - startPoint) &&
            x < (SettingProvider.getCanvasWidth() - startPoint)
    }

    override fun drawRectangle(graphicsContext: GraphicsContext) {
        graphicsContext.fill = backGroundColor
        graphicsContext.fillRect(
            SettingProvider.getCanvasWidth() - WIDTH_AXIS - startPoint,
            0.0,
            WIDTH_AXIS,
            SettingProvider.getCanvasHeight()
        )
    }

    override fun drawMinorDelimiters(graphicsContext: GraphicsContext) {
        graphicsContext.stroke = delimiterColor
        var current = 0.0
        while (current < SettingProvider.getCanvasHeight() - WIDTH_AXIS) {
            graphicsContext.strokeLine(SettingProvider.getCanvasWidth() - startPoint - WIDTH_AXIS + WIDTH_DELIMITER, current,
                SettingProvider.getCanvasWidth() - startPoint - WIDTH_AXIS, current)
            current += minDelta
        }
    }

    override fun drawDeltaMarks(graphicsContext: GraphicsContext) {
        val normalMarks = deltaMarks.reversed()

        var current = 0.0
        var i = 0
        while (current < SettingProvider.getCanvasHeight() && i < normalMarks.size) {
            graphicsContext.strokeText(
                "%.2f".format(normalMarks[i]), SettingProvider.getCanvasWidth() - TEXT_ALIGN - startPoint,
                current - WIDTH_AXIS
            )
            i++
            current += minDelta * DEFAULT_DELTA_SPACE
        }
    }

    override fun getDirection(): Direction {
        return Direction.RIGHT
    }

    private companion object {
        const val TEXT_ALIGN = 30.0
    }
}