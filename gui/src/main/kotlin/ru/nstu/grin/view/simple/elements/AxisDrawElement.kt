package ru.nstu.grin.view.simple.elements

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.view.ChainDrawElement

class AxisDrawElement : ChainDrawElement {
    override fun draw(context: GraphicsContext) {
        context.stroke = Color.valueOf("383838")

        val middleHeight = context.canvas.height / 2
        context.strokeLine(0.0, middleHeight, context.canvas.width, middleHeight)

        val middleWith = context.canvas.width / 2
        context.strokeLine(middleWith, 0.0, middleWith, context.canvas.height)
    }
}