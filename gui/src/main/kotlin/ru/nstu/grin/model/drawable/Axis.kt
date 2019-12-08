package ru.nstu.grin.model.drawable

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.model.Direction
import ru.nstu.grin.model.Drawable
import ru.nstu.grin.settings.SettingProvider

/**
 * @author kostya05983
 * This class is responsible to draw axis
 */
class Axis(
    private val minDelta: Double,
    private val deltaMarks: List<Double>,
    private val position: Direction,
    private val backGroundColor: Color,
    private val delimiterColor: Color
) : Drawable {
    override fun draw(context: GraphicsContext) {
        drawRectangle(context)
        drawMinorDelimiters(context)
        drawDeltaMarks(context)
    }

    private fun drawRectangle(graphicsContext: GraphicsContext) {
        graphicsContext.fill = backGroundColor
        when (position) {
            Direction.LEFT -> graphicsContext.fillRect(0.0, 0.0, WIDTH_AXIS, SettingProvider.getCanvasHeight())
            Direction.RIGHT -> graphicsContext.fillRect(SettingProvider.getCanvasWidth() - WIDTH_AXIS, 0.0, WIDTH_AXIS, SettingProvider.getCanvasHeight())
            Direction.TOP -> graphicsContext.fillRect(0.0, 0.0, SettingProvider.getCanvasWidth(), WIDTH_AXIS)
            Direction.BOTTOM -> graphicsContext.fillRect(0.0, SettingProvider.getCanvasHeight() - WIDTH_AXIS, SettingProvider.getCanvasWidth(), SettingProvider.getCanvasHeight())
        }
    }

    private fun drawMinorDelimiters(graphicsContext: GraphicsContext) {
        graphicsContext.stroke = delimiterColor
        var current = 0.0
        while (current < SettingProvider.getCanvasHeight() - WIDTH_AXIS) {
            graphicsContext.strokeLine(WIDTH_AXIS - WIDTH_DELIMITER, current, WIDTH_AXIS, current)
            current += minDelta
        }
        current = WIDTH_AXIS
        while (current < SettingProvider.getCanvasWidth()) {
            graphicsContext.strokeLine(current, SettingProvider.getCanvasHeight() - (WIDTH_AXIS - WIDTH_DELIMITER), current, SettingProvider.getCanvasHeight() - WIDTH_AXIS)
            current += minDelta
        }
    }

    private fun stabilizeMarks(): List<Double> {
        return when (position) {
            Direction.LEFT -> {
                deltaMarks.reversed()
            }
            Direction.RIGHT -> {
                deltaMarks.reversed()
            }
            Direction.TOP -> {
                deltaMarks
            }
            Direction.BOTTOM -> {
                deltaMarks
            }
        }
    }

    private fun drawDeltaMarks(graphicsContext: GraphicsContext) {
        val stabilizedMarks = stabilizeMarks()

        var current = 0.0
        var i = 0
        when (position) {
            Direction.LEFT -> {
                while (current < SettingProvider.getCanvasWidth() && i < stabilizedMarks.size) {
                    graphicsContext.strokeText(stabilizedMarks[i].toString(), WIDTH_AXIS - TEXT_ALIGN,
                        current - WIDTH_AXIS)
                    i++
                    current += minDelta * DEFAULT_DELTA_SPACE
                }
            }
            Direction.RIGHT -> TODO()
            Direction.TOP -> TODO()
            Direction.BOTTOM -> {
                while (current < SettingProvider.getCanvasWidth() && i < stabilizedMarks.size) {
                    graphicsContext.strokeText(stabilizedMarks[i].toString(), WIDTH_AXIS + current,
                        SettingProvider.getCanvasHeight() - WIDTH_AXIS + TEXT_ALIGN)
                    i++
                    current += minDelta * DEFAULT_DELTA_SPACE
                }
            }
        }
    }

    internal companion object {
        const val WIDTH_AXIS = 50.0 // 100 px in default
        const val WIDTH_DELIMITER = 10.0
        const val TEXT_ALIGN = 30.0
        private const val DEFAULT_DELTA_SPACE = 5
    }
}