package ru.nstu.grin.model.drawable

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.model.Drawable

/**
 * @author kostya05983
 */
data class Description(
    val title: String,
    val size: Int,
    val font: String,
    val x: Double,
    val y: Double
) : Drawable {
    override fun draw(context: GraphicsContext) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}