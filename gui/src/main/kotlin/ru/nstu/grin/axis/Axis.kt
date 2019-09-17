package ru.nstu.grin.axis

import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import ru.nstu.grin.Direction

/**
 * @author kostya05983
 * This class is responsible to draw axis
 */
class Axis(
    private val canvas: Canvas,
    private val delta: Double,
    private val minDelta: Double,
    private val deltas: List<String>,
    private val position: Direction,
    private val backGroundColor: Color,
    private val delimiterColor: Color
) {
    companion object {
        private const val WIDTH_AXIS = 50.0 // 100 px in default
        private const val WIDTH_DELIMITER = 10.0
        private const val TEXT_ALIGN = 30.0
    }

    var label: String? = null

    init {
        val gc = canvas.graphicsContext2D
        gc.fill = backGroundColor //set color
        when (position) {
            Direction.TOP -> {
                gc.fillRect(0.0, 0.0, gc.canvas.width, WIDTH_AXIS)
            }
            Direction.LEFT -> {
                gc.fillRect(0.0, 0.0, WIDTH_AXIS, gc.canvas.height)
            }
            Direction.RIGHT -> {
                gc.fillRect(gc.canvas.width - WIDTH_AXIS, 0.0, WIDTH_AXIS, gc.canvas.height)
            }
            Direction.BOTTOM -> {
                gc.fillRect(0.0, gc.canvas.height - WIDTH_AXIS, gc.canvas.width, gc.canvas.height)
            }
        }
        drawMinorDelimiters()
        drawMajorDelta()
    }

    private fun drawMinorDelimiters() {
        val gc = canvas.graphicsContext2D
        gc.stroke = delimiterColor

        val maxHeight = canvas.height
        var current = 0.0
        while (current < maxHeight) {
            gc.strokeLine(WIDTH_AXIS - WIDTH_DELIMITER, current, WIDTH_AXIS, current)
            current += minDelta
        }
    }

    private fun drawMajorDelta() {
        val gc = canvas.graphicsContext2D
        gc.fill = delimiterColor
        val reversedList = deltas.reversed()

        val maxHeight = canvas.height
        var current = 0.0
        var i = 0
        while (current < maxHeight) {
            if (i < reversedList.size) {
                gc.strokeText(reversedList[i], WIDTH_AXIS - TEXT_ALIGN, current, 20.0)
            }
            i++
            current += delta
        }
    }
}