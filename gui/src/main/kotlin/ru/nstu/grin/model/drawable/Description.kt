package ru.nstu.grin.model.drawable

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.model.CoordinateDirection
import ru.nstu.grin.model.Drawable
import sun.security.krb5.internal.crypto.Des

/**
 * @author kostya05983
 */
data class Description(
    val text: String,
    val size: Double,
    val x: Double,
    val y: Double,
    val color: Color
) : Drawable {
    override fun scale(scale: Double, direction: CoordinateDirection): Drawable {
        return when (direction) {
            CoordinateDirection.X -> Description(
                text, size, x * scale, y, color
            )
            CoordinateDirection.Y -> Description(
                text, size, x, y * scale, color
            )
        }
    }

    override fun draw(context: GraphicsContext) {
        context.stroke = color
        context.strokeText(text, x, y, size)
    }
}