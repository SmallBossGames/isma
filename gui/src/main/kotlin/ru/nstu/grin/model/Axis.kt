package ru.nstu.grin.model

import javafx.scene.canvas.GraphicsContext
import tornadofx.*
import javafx.scene.paint.Color
import ru.nstu.grin.Direction

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
    private val delimiterColor: Color,
    private val canvasHeight: Double,
    private val canvasWidth: Double
) : Drawable {
    override fun draw(context: GraphicsContext) {
        drawRectangle(context, canvasWidth, canvasHeight)
        drawMinorDelimiters(context, canvasHeight)
        drawDeltaMarks(context, canvasHeight)
    }

    private fun drawRectangle(graphicsContext: GraphicsContext, width: Double, height: Double) {
        graphicsContext.fill = backGroundColor
        when (position) {
            Direction.LEFT -> graphicsContext.fillRect(0.0, 0.0, WIDTH_AXIS, height)
            Direction.RIGHT -> graphicsContext.fillRect(width - WIDTH_AXIS, 0.0, WIDTH_AXIS, height)
            Direction.TOP -> graphicsContext.fillRect(0.0, 0.0, width, WIDTH_AXIS)
            Direction.BOTTOM -> graphicsContext.fillRect(0.0, height - WIDTH_AXIS, width, height)
        }
    }

    private fun drawMinorDelimiters(graphicsContext: GraphicsContext, canvasHeight: Double) {
        graphicsContext.stroke = delimiterColor
        var current = 0.0
        while (current < canvasHeight) {
            graphicsContext.strokeLine(WIDTH_AXIS - WIDTH_DELIMITER, current, WIDTH_AXIS, current)
            current += minDelta
        }
    }

    private fun drawDeltaMarks(graphicsContext: GraphicsContext, canvasHeight: Double) {
        val reversedList = deltas.reversed()

        var current = 0.0
        var i = 0
        while (current < canvasHeight) {
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