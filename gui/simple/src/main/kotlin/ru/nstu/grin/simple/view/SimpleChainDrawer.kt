package ru.nstu.grin.simple.view

import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import ru.nstu.grin.common.draw.elements.ArrowDrawElement
import ru.nstu.grin.common.draw.elements.ClearDrawElement
import ru.nstu.grin.common.draw.elements.DescriptionDrawElement
import ru.nstu.grin.common.view.ChainDrawer
import ru.nstu.grin.simple.draw.elements.AxisDrawElement
import ru.nstu.grin.simple.draw.elements.FunctionsDrawElement
import ru.nstu.grin.simple.draw.elements.GridDrawElement
import ru.nstu.grin.simple.draw.elements.MarkersDrawElement
import ru.nstu.grin.simple.model.view.SimpleCanvasViewModel

class SimpleChainDrawer(
    private val canvas: Canvas,
    private val model: SimpleCanvasViewModel
) : ChainDrawer {
    override fun draw() {
        val gridSize = model.settings.pixelCost

        val context = canvas.graphicsContext2D
        ClearDrawElement().draw(context)
        ArrowDrawElement(model.arrows, 1.0).draw(context)
        DescriptionDrawElement(model.descriptions).draw(context)
        GridDrawElement(gridSize / 5, Color.valueOf("EDEDED"), model.settings).draw(context)
        GridDrawElement(gridSize, Color.valueOf("BBBBBB"), model.settings).draw(context)
        AxisDrawElement(model.settings).draw(context)
        MarkersDrawElement(model.settings).draw(context)
        FunctionsDrawElement(model.settings, model.functions).draw(context)
    }
}