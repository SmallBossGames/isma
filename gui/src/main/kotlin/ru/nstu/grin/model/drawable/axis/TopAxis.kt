package ru.nstu.grin.model.drawable.axis

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.model.CoordinateDirection
import ru.nstu.grin.model.Direction
import ru.nstu.grin.model.DraggedDirection
import ru.nstu.grin.model.Drawable
import ru.nstu.grin.settings.SettingsProvider

data class TopAxis(
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
        return TopAxis(
            startPoint, minDelta, newDeltas, backGroundColor, delimiterColor
        )
    }

    override fun isOnIt(x: Double, y: Double): Boolean {
        return y < WIDTH_AXIS + startPoint && y > startPoint
    }

    override fun changeDeltas(value: Double, direction: DraggedDirection): AbstractAxis {
        return when (direction) {
            DraggedDirection.LEFT -> {
                TopAxis(
                    startPoint,
                    minDelta,
                    deltaMarks.map { it + value },
                    backGroundColor,
                    delimiterColor
                )
            }
            DraggedDirection.RIGHT -> {
                TopAxis(
                    startPoint,
                    minDelta,
                    deltaMarks.map { it - value },
                    backGroundColor,
                    delimiterColor
                )
            }
            DraggedDirection.UP, DraggedDirection.DOWN, DraggedDirection.UNDEFINED -> {
                this
            }
        }
    }

    override fun drawRectangle(graphicsContext: GraphicsContext) {
        graphicsContext.fill = backGroundColor
        graphicsContext.fillRect(0.0, startPoint, SettingsProvider.getCanvasWidth(), WIDTH_AXIS)
    }

    override fun drawMinorDelimiters(graphicsContext: GraphicsContext) {
        graphicsContext.stroke = delimiterColor
        var current = 0.0
        while (current < SettingsProvider.getCanvasWidth()) {
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
        while (current < SettingsProvider.getCanvasWidth() && i < deltaMarks.size) {
            graphicsContext.strokeText(
                "%.2f".format(deltaMarks[i]), current,
                TEXT_ALIGN + startPoint
            )
            i++
            current += minDelta * DEFAULT_DELTA_SPACE
        }
    }

    override fun getDirection(): Direction {
        return Direction.TOP
    }

    private companion object {
        const val TEXT_ALIGN = 30.0
    }
}