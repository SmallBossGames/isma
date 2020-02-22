package ru.nstu.grin.view.simple

import javafx.scene.canvas.Canvas
import ru.nstu.grin.model.view.SimpleCanvasViewModel
import ru.nstu.grin.view.ChainDrawer

class SimpleChainDrawer(
    private val canvas: Canvas,
    private val model: SimpleCanvasViewModel
) : ChainDrawer {
    override fun draw() {
        val context = canvas.graphicsContext2D
        AxisDrawElement().draw(context)
        FunctionsDrawElement(model.functions).draw(context)
        ArrowDrawElement(model.arrows).draw(context)
    }
}