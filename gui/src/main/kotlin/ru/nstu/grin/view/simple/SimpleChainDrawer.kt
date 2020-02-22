package ru.nstu.grin.view.simple

import javafx.scene.canvas.Canvas
import ru.nstu.grin.model.view.SimpleCanvasViewModel
import ru.nstu.grin.view.ChainDrawer

class SimpleChainDrawer(
    private val canvas: Canvas,
    private val model: SimpleCanvasViewModel
) : ChainDrawer {
    private val axisDrawElement = AxisDrawElement()
    private val functionsDrawElement = FunctionsDrawElement(model)
    private val arrowDrawElement = ArrowDrawElement(model)


    override fun draw() {
        axisDrawElement.draw(canvas.graphicsContext2D)
        functionsDrawElement.draw(canvas.graphicsContext2D)
        arrowDrawElement.draw(canvas.graphicsContext2D)
    }
}