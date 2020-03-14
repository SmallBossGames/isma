package ru.nstu.grin.view.concatenation

import javafx.scene.canvas.Canvas
import ru.nstu.grin.draw.elements.ArrowDrawElement
import ru.nstu.grin.draw.elements.ClearDrawElement
import ru.nstu.grin.draw.elements.DescriptionDrawElement
import ru.nstu.grin.draw.elements.concatenation.AxisDrawElement
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
        ArrowDrawElement(model.arrows, 1.0).draw(context)
        DescriptionDrawElement(model.descriptions).draw(context)

        for (cartesianSpace in model.cartesianSpace) {
            AxisDrawElement(cartesianSpace.xAxis, cartesianSpace.yAxis).draw(context)
            ConcatenationFunctionDrawElement(cartesianSpace.functions, cartesianSpace.settings).draw(context)
        }
    }
}