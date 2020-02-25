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
        val gridSize = model.settings.pixelCost

        val context = canvas.graphicsContext2D
        ClearDrawElement().draw(context)
        ArrowDrawElement(model.arrows).draw(context)
        DescriptionDrawElement(model.descriptions).draw(context)
        GridDrawElement(gridSize / 5, Color.valueOf("EDEDED")).draw(context)
        GridDrawElement(gridSize, Color.valueOf("BBBBBB")).draw(context)
        AxisDrawElement().draw(context)
        MarkersDrawElement(model.settings).draw(context)
        FunctionsDrawElement(model.settings, model.functions).draw(context)
    }
}