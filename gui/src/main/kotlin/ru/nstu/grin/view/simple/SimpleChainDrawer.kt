package ru.nstu.grin.view.simple

import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import ru.nstu.grin.model.view.SimpleCanvasViewModel
import ru.nstu.grin.view.ChainDrawer
import ru.nstu.grin.view.simple.elements.*

class SimpleChainDrawer(
    private val canvas: Canvas,
    private val model: SimpleCanvasViewModel
) : ChainDrawer {
    override fun draw() {
        val context = canvas.graphicsContext2D
        FunctionsDrawElement(model.functions).draw(context)
        ArrowDrawElement(model.arrows).draw(context)
        DescriptionDrawElement(model.descriptions).draw(context)
        GridDrawElement(24, Color.valueOf("EDEDED")).draw(context)
        GridDrawElement(120, Color.valueOf("BBBBBB")).draw(context)
        AxisDrawElement().draw(context)
    }
}