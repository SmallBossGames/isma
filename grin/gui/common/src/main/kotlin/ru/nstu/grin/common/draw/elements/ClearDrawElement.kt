package ru.nstu.grin.common.draw.elements

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.common.view.ChainDrawElement

object ClearDrawElement : ChainDrawElement {
    override fun draw(context: GraphicsContext, canvasWidth: Double, canvasHeight: Double) {
        context.clearRect(0.0, 0.0, canvasWidth, canvasHeight)
    }
}