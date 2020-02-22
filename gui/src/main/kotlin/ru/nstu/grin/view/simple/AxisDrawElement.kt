package ru.nstu.grin.view.simple

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.view.ChainDrawElement

class AxisDrawElement : ChainDrawElement {
    override fun draw(graphicsContext: GraphicsContext) {
        val middleHeight = graphicsContext.canvas.height / 2
        graphicsContext.strokeLine(0.0, middleHeight, graphicsContext.canvas.width, middleHeight)

        val middleWith = graphicsContext.canvas.width / 2
        graphicsContext.strokeLine(middleWith, 0.0, middleWith, graphicsContext.canvas.height)
    }
}