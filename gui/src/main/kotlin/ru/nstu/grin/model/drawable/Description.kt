package ru.nstu.grin.model.drawable

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.model.Drawable

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
    override fun draw(context: GraphicsContext) {
        context.stroke = color
        context.strokeText(text, x, y, size)
    }
}