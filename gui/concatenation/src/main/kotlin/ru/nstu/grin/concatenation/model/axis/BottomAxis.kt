package ru.nstu.grin.concatenation.model.axis

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.concatenation.model.CoordinateDirection
import ru.nstu.grin.concatenation.model.Direction
import ru.nstu.grin.concatenation.model.DraggedDirection
import ru.nstu.grin.concatenation.model.Drawable

/**
 * @author Konstantin Volivach
 */
data class BottomAxis(
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
        return BottomAxis(
            startPoint, minDelta, newDeltas, backGroundColor, delimiterColor
        )
    }

    override fun changeDeltas(value: Double, direction: DraggedDirection): AbstractAxis {
        return when (direction) {
            DraggedDirection.LEFT -> {
                BottomAxis(
                    startPoint,
                    minDelta,
                    deltaMarks.map { it + value },
                    backGroundColor,
                    delimiterColor
                )
            }
            DraggedDirection.RIGHT -> {
                BottomAxis(
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

    override fun isOnIt(x: Double, y: Double): Boolean {
        return y > SettingsProvider.getCanvasHeight() - WIDTH_AXIS - startPoint &&
            y < SettingsProvider.getCanvasHeight() - startPoint
    }

    override fun drawRectangle(graphicsContext: GraphicsContext) {
        graphicsContext.fill = backGroundColor
        graphicsContext.fillRect(
            0.0,
            SettingsProvider.getCanvasHeight() - WIDTH_AXIS - startPoint,
            SettingsProvider.getCanvasWidth(),
            WIDTH_AXIS
        )
    }

    override fun drawMinorDelimiters(graphicsContext: GraphicsContext) {
        graphicsContext.stroke = delimiterColor

        var current = WIDTH_AXIS
        while (current < SettingsProvider.getCanvasWidth()) {
            graphicsContext.strokeLine(
                current,
                SettingsProvider.getCanvasHeight() - (WIDTH_AXIS - WIDTH_DELIMITER) - startPoint,
                current,
                SettingsProvider.getCanvasHeight() - WIDTH_AXIS - startPoint
            )
            current += minDelta
        }
    }

    override fun drawDeltaMarks(graphicsContext: GraphicsContext) {
        var current = 0.0
        var i = 0
        while (current < SettingsProvider.getCanvasWidth() && i < deltaMarks.size) {
            graphicsContext.strokeText(
                "%.2f".format(deltaMarks[i]), WIDTH_AXIS + current,
                SettingsProvider.getCanvasHeight() - WIDTH_AXIS + TEXT_ALIGN - startPoint
            )
            i++
            current += minDelta * DEFAULT_DELTA_SPACE
        }
    }

    override fun getDirection(): Direction {
        return Direction.BOTTOM
    }

    private companion object {
        const val TEXT_ALIGN = 30.0
    }
}