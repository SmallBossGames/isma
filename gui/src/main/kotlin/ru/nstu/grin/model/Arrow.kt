package ru.nstu.grin.model

import javafx.scene.Node
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.shape.Path
import tornadofx.add

/**
 * @author kostya05983
 */
class Arrow(
    var color: Color,
    val x: Double,
    val y: Double
) {
    fun getShape(): Node {
        val path = Path()

        val base = Line(x, y, x + DEFAULT_LENGTH, y + DEFAULT_LENGTH)
        path.add(base)

        val left = Line(x + DEFAULT_LENGTH, y + DEFAULT_LENGTH, x + DEFAULT_LENGTH / 2, y + DEFAULT_LENGTH)
        path.add(left)

        val right = Line(x + DEFAULT_LENGTH, y + DEFAULT_LENGTH, x + DEFAULT_LENGTH * 2, y + DEFAULT_LENGTH)
        path.add(right)

        return path
    }

    private companion object {
        const val DEFAULT_LENGTH = 10
    }
}