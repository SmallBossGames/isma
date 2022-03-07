package ru.nstu.grin.common.draw.elements

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.common.model.Arrow
import ru.nstu.grin.common.view.ChainDrawElement

class ArrowDrawElement(
    private val arrows: List<Arrow>,
    private val pixelCost: Double
) : ChainDrawElement {
    override fun draw(context: GraphicsContext, canvasWidth: Double, canvasHeight: Double) {
        for (arrow in arrows) {
            context.stroke = arrow.color
            val x = arrow.x * pixelCost
            val y = arrow.y * pixelCost
            context.strokeLine(x, y, x + DEFAULT_LENGTH, y + DEFAULT_LENGTH)
            context.strokeLine(
                x + DEFAULT_LENGTH,
                y + DEFAULT_LENGTH,
                x + DEFAULT_LENGTH / 2,
                y + DEFAULT_LENGTH
            )
            context.strokeLine(
                y + DEFAULT_LENGTH,
                y + DEFAULT_LENGTH,
                x + DEFAULT_LENGTH,
                y + DEFAULT_LENGTH / 2
            )
        }
    }

    private companion object {
        const val DEFAULT_LENGTH = 20
    }
}