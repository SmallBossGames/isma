package ru.nstu.grin.concatenation.draw

import javafx.scene.canvas.Canvas
import ru.nstu.grin.common.draw.elements.ArrowDrawElement
import ru.nstu.grin.common.draw.elements.ClearDrawElement
import ru.nstu.grin.common.draw.elements.DescriptionDrawElement
import ru.nstu.grin.common.view.ChainDrawer
import ru.nstu.grin.concatenation.draw.elements.AxisDrawElement
import ru.nstu.grin.concatenation.draw.elements.ConcatenationFunctionDrawElement
import ru.nstu.grin.concatenation.model.view.ConcatenationCanvasModelViewModel

class ConcatenationChainDrawer(
    private val canvas: Canvas,
    private val model: ConcatenationCanvasModelViewModel
) : ChainDrawer {
    override fun draw() {
        val context = canvas.graphicsContext2D
        ClearDrawElement().draw(context)
        ArrowDrawElement(model.arrows, 1.0).draw(context)
        DescriptionDrawElement(model.descriptions).draw(context)

        for (cartesianSpace in model.cartesianSpaces) {
            AxisDrawElement(
                cartesianSpace.xAxis,
                cartesianSpace.yAxis,
                cartesianSpace.settings,
                model.cartesianSpaces
            ).draw(context)
            ConcatenationFunctionDrawElement(
                cartesianSpace.functions,
                cartesianSpace.xAxis,
                cartesianSpace.yAxis,
                cartesianSpace.settings
            ).draw(context)
        }
    }
}