package ru.nstu.grin.model

import javafx.scene.canvas.GraphicsContext
import tornadofx.*
import javafx.scene.paint.Color
import ru.nstu.grin.Direction
import ru.nstu.grin.settings.SettingProvider

/**
 * @author kostya05983
 * This class is responsible to draw axis
 */
class Axis(
    private val delta: Double,
    private val minDelta: Double,
    private val deltas: List<String>,
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
        while (current < SettingProvider.getCanvasHeight()) {
            graphicsContext.strokeLine(WIDTH_AXIS - WIDTH_DELIMITER, current, WIDTH_AXIS, current)
            current += minDelta
        }
    }

    private fun drawDeltaMarks(graphicsContext: GraphicsContext) {
        val reversedList = deltas.reversed()

        var current = 0.0
        var i = 0
        while (current < SettingProvider.getCanvasHeight()) {
            if (i < reversedList.size) {
                graphicsContext.strokeText(reversedList[i], WIDTH_AXIS - TEXT_ALIGN, current)
            }
            i++
            current += delta
        }
    }

    private companion object {
        const val WIDTH_AXIS = 50.0 // 100 px in default
        const val WIDTH_DELIMITER = 10.0
        const val TEXT_ALIGN = 30.0
    }
}