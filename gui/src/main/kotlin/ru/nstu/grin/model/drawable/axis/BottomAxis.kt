package ru.nstu.grin.model.drawable.axis

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.model.CoordinateDirection
import ru.nstu.grin.model.Direction
import ru.nstu.grin.model.Drawable
import ru.nstu.grin.settings.SettingProvider

/**
 * @author Konstantin Volivach
 */
class BottomAxis(
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

    override fun isOnIt(x: Double, y: Double): Boolean {
        return y > SettingProvider.getCanvasHeight() - WIDTH_AXIS - startPoint &&
            y < SettingProvider.getCanvasHeight() - startPoint
    }

    override fun drawRectangle(graphicsContext: GraphicsContext) {
        graphicsContext.fill = backGroundColor
        graphicsContext.fillRect(
            0.0,
            SettingProvider.getCanvasHeight() - WIDTH_AXIS - startPoint,
            SettingProvider.getCanvasWidth(),
            WIDTH_AXIS
        )
    }

    override fun drawMinorDelimiters(graphicsContext: GraphicsContext) {
        graphicsContext.stroke = delimiterColor

        var current = WIDTH_AXIS
        while (current < SettingProvider.getCanvasWidth()) {
            graphicsContext.strokeLine(
                current,
                SettingProvider.getCanvasHeight() - (WIDTH_AXIS - WIDTH_DELIMITER) - startPoint,
                current,
                SettingProvider.getCanvasHeight() - WIDTH_AXIS - startPoint
            )
            current += minDelta
        }
    }

    override fun drawDeltaMarks(graphicsContext: GraphicsContext) {
        var current = 0.0
        var i = 0
        while (current < SettingProvider.getCanvasWidth() && i < deltaMarks.size) {
            graphicsContext.strokeText(
                "%.2f".format(deltaMarks[i]), WIDTH_AXIS + current,
                SettingProvider.getCanvasHeight() - WIDTH_AXIS + TEXT_ALIGN - startPoint
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