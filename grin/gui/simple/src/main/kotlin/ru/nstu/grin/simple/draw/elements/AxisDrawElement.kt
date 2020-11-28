package ru.nstu.grin.simple.draw.elements

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.simple.view.SimplePlotSettings

class AxisDrawElement(
    private val settings: SimplePlotSettings
) : ChainDrawElement {
    override fun draw(context: GraphicsContext) {
        context.stroke = Color.valueOf("383838")

        val middleHeight = context.canvas.height / 2 + settings.yCorrelation
        context.strokeLine(0.0, middleHeight, context.canvas.width, middleHeight)

        val middleWidth = context.canvas.width / 2 + settings.xCorrelation
        context.strokeLine(middleWidth, 0.0, middleWidth, context.canvas.height)
    }
}