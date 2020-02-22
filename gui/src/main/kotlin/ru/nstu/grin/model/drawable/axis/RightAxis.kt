package ru.nstu.grin.model.drawable.axis

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.model.CoordinateDirection
import ru.nstu.grin.model.Direction
import ru.nstu.grin.model.DraggedDirection
import ru.nstu.grin.model.Drawable
import ru.nstu.grin.settings.SettingsProvider

data class RightAxis(
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

    override fun changeDeltas(value: Double, direction: DraggedDirection): AbstractAxis {
        return when (direction) {
            DraggedDirection.DOWN -> {
                RightAxis(
                    startPoint,
                    minDelta,
                    deltaMarks.map { it + value },
                    backGroundColor,
                    delimiterColor
                )
            }
            DraggedDirection.UP -> {
                RightAxis(
                    startPoint,
                    minDelta,
                    deltaMarks.map { it - value },
                    backGroundColor,
                    delimiterColor
                )
            }
            DraggedDirection.UNDEFINED, DraggedDirection.LEFT, DraggedDirection.RIGHT -> {
                this
            }
        }
    }

    override fun isOnIt(x: Double, y: Double): Boolean {
        return x > (SettingsProvider.getCanvasWidth() - WIDTH_AXIS - startPoint) &&
            x < (SettingsProvider.getCanvasWidth() - startPoint)
    }

    override fun drawRectangle(graphicsContext: GraphicsContext) {
        graphicsContext.fill = backGroundColor
        graphicsContext.fillRect(
            SettingsProvider.getCanvasWidth() - WIDTH_AXIS - startPoint,
            0.0,
            WIDTH_AXIS,
            SettingsProvider.getCanvasHeight()
        )
    }

    override fun drawMinorDelimiters(graphicsContext: GraphicsContext) {
        graphicsContext.stroke = delimiterColor
        var current = 0.0
        while (current < SettingsProvider.getCanvasHeight() - WIDTH_AXIS) {
            graphicsContext.strokeLine(SettingsProvider.getCanvasWidth() - startPoint - WIDTH_AXIS + WIDTH_DELIMITER, current,
                SettingsProvider.getCanvasWidth() - startPoint - WIDTH_AXIS, current)
            current += minDelta
        }
    }

    override fun drawDeltaMarks(graphicsContext: GraphicsContext) {
        val normalMarks = deltaMarks.reversed()

        var current = 0.0
        var i = 0
        while (current < SettingsProvider.getCanvasHeight() && i < normalMarks.size) {
            graphicsContext.strokeText(
                "%.2f".format(normalMarks[i]), SettingsProvider.getCanvasWidth() - TEXT_ALIGN - startPoint,
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