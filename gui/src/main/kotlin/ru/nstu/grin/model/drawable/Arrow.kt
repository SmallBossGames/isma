package ru.nstu.grin.model.drawable

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.extensions.toByteArray
import ru.nstu.grin.file.Writer
import ru.nstu.grin.model.CoordinateDirection
import ru.nstu.grin.model.Drawable
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

/**
 * @author kostya05983
 * Class represents arrow on plot
 * Help to show how description relative to function
 */
class Arrow(
    var color: Color,
    val x: Double,
    val y: Double
) : Drawable, Writer {
    override fun scale(scale: Double, direction: CoordinateDirection): Drawable {
        return when (direction) {
            CoordinateDirection.X -> {
                Arrow(
                    color = color,
                    x = x * scale,
                    y = y
                )
            }
            CoordinateDirection.Y -> {
                Arrow(
                    color = color,
                    x = x,
                    y = y * scale
                )
            }
        }
    }

    override fun isOnIt(x: Double, y: Double): Boolean {
        return true
    }

    override fun draw(context: GraphicsContext) {
        context.stroke = color
        context.strokeLine(x, y, x + DEFAULT_LENGTH, y + DEFAULT_LENGTH)
        context.strokeLine(x + DEFAULT_LENGTH, y + DEFAULT_LENGTH, x + DEFAULT_LENGTH / 2, y + DEFAULT_LENGTH)
        context.strokeLine(x + DEFAULT_LENGTH, y + DEFAULT_LENGTH, x + DEFAULT_LENGTH, y + DEFAULT_LENGTH / 2)
    }

    /**
     * color
     * x
     * y
     */
    override fun serialize(): ByteArray {
        return ByteArrayOutputStream().use { baos ->
            ObjectOutputStream(baos).use {
                it.write(color.toByteArray())
                it.writeDouble(x)
                it.writeDouble(y)
                it.flush()
            }
            baos
        }.toByteArray()
    }

    private companion object {
        const val DEFAULT_LENGTH = 20
    }
}