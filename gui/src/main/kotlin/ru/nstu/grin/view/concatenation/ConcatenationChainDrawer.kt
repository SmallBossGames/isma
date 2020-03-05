package ru.nstu.grin.view.concatenation

import javafx.scene.canvas.Canvas
import ru.nstu.grin.draw.elements.ArrowDrawElement
import ru.nstu.grin.draw.elements.ClearDrawElement
import ru.nstu.grin.draw.elements.DescriptionDrawElement
import ru.nstu.grin.draw.elements.concatenation.ConcatenationFunctionDrawElement
import ru.nstu.grin.model.view.ConcatenationCanvasModelViewModel
import ru.nstu.grin.view.ChainDrawer

class ConcatenationChainDrawer(
    private val canvas: Canvas,
    private val model: ConcatenationCanvasModelViewModel
) : ChainDrawer {
    override fun draw() {
        val context = canvas.graphicsContext2D
        ClearDrawElement().draw(context)
        ArrowDrawElement(model.arrows).draw(context)
        DescriptionDrawElement(model.descriptions).draw(context)
        ConcatenationFunctionDrawElement(model.functions).draw(context)
    }
}