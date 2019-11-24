package ru.nstu.grin.model

import javafx.scene.Node
import javafx.scene.canvas.GraphicsContext
import tornadofx.*
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.shape.Path
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text
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
    private val delimiterColor: Color
) : Drawable {


    fun getShape(canvasWidth: Double, canvasHeight: Double): Node {
        val path = Path()

        val rectangle = getRectangle(canvasWidth, canvasHeight)
        path.add(rectangle)

        val delimiters = getMinorDelimiters(canvasHeight)
        for (delimiter in delimiters) {
            path.add(delimiter)
        }

        val marks = getDeltaMarks(canvasHeight)
        for (mark in marks) {
            path.add(mark)
        }
        return path
    }

    private fun getRectangle(width: Double, height: Double): Rectangle {
        return when (position) {
            Direction.LEFT -> Rectangle(0.0, 0.0, WIDTH_AXIS, height)
            Direction.RIGHT -> Rectangle(width - WIDTH_AXIS, 0.0, WIDTH_AXIS, height)
            Direction.TOP -> Rectangle(0.0, 0.0, width, WIDTH_AXIS)
            Direction.BOTTOM -> Rectangle(0.0, height - WIDTH_AXIS, width, height)
        }
    }

    private fun getMinorDelimiters(canvasHeight: Double): List<Line> {
        val lines = mutableListOf<Line>()
        var current = 0.0
        while (current < canvasHeight) {
            lines.add(Line(WIDTH_AXIS - WIDTH_DELIMITER, current, WIDTH_AXIS, current))
            current += minDelta
        }
        return lines
    }

    private fun getDeltaMarks(canvasHeight: Double): List<Text> {
        val marks = mutableListOf<Text>()

        val reversedList = deltas.reversed()

        var current = 0.0
        var i = 0
        while (current < canvasHeight) {
            if (i < reversedList.size) {
                marks.add(
                    Text(WIDTH_AXIS - TEXT_ALIGN, current, reversedList[i])
                )
            }
            i++
            current += delta
        }
        return marks
    }

    private companion object {
        const val WIDTH_AXIS = 50.0 // 100 px in default
        const val WIDTH_DELIMITER = 10.0
        const val TEXT_ALIGN = 30.0
    }

    override fun draw(context: GraphicsContext) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}