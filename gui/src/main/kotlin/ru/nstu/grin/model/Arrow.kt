package ru.nstu.grin.model

import javafx.scene.Node
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path

/**
 * @author kostya05983
 */
class Arrow(
    var color: Color,
    val x: Double,
    val y: Double
) : Drawable {
    override fun draw(context: GraphicsContext) {
        context.stroke = color
        context.strokeLine(x, y, x + DEFAULT_LENGTH, y + DEFAULT_LENGTH)
        context.strokeLine(x + DEFAULT_LENGTH, y + DEFAULT_LENGTH, x + DEFAULT_LENGTH / 2, y + DEFAULT_LENGTH)
        context.strokeLine(x + DEFAULT_LENGTH, y + DEFAULT_LENGTH, x + DEFAULT_LENGTH, y + DEFAULT_LENGTH / 2)
    }

    fun getShape(): Node {
        val path = Path()

        val moveTo = MoveTo(x, y)
        val base = LineTo(x + DEFAULT_LENGTH, y + DEFAULT_LENGTH)

        val leftMoveTo = MoveTo(x + DEFAULT_LENGTH, y + DEFAULT_LENGTH)
        val left = LineTo(x + DEFAULT_LENGTH / 2, y + DEFAULT_LENGTH)
        val rightMoveTo = MoveTo(x + DEFAULT_LENGTH, y + DEFAULT_LENGTH)
        val right = LineTo(x + DEFAULT_LENGTH, y + DEFAULT_LENGTH / 2)

        path.elements.addAll(moveTo, base, leftMoveTo, left, rightMoveTo, right)
        return path
    }

    private companion object {
        const val DEFAULT_LENGTH = 20
    }
}