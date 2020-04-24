package ru.nstu.grin.concatenation.draw

import javafx.scene.canvas.Canvas
import javafx.scene.control.ContextMenu
import javafx.scene.control.Tooltip
import ru.nstu.grin.common.draw.elements.ArrowDrawElement
import ru.nstu.grin.common.draw.elements.ClearDrawElement
import ru.nstu.grin.common.draw.elements.DescriptionDrawElement
import ru.nstu.grin.common.view.ChainDrawer
import ru.nstu.grin.concatenation.controller.ConcatenationCanvasController
import ru.nstu.grin.concatenation.draw.elements.axis.AxisDrawElement
import ru.nstu.grin.concatenation.draw.elements.ConcatenationFunctionDrawElement
import ru.nstu.grin.concatenation.draw.elements.ContextMenuDrawElement
import ru.nstu.grin.concatenation.draw.elements.TooltipsDrawElement
import ru.nstu.grin.concatenation.model.view.ConcatenationCanvasModelViewModel

class ConcatenationChainDrawer(
    private val canvas: Canvas,
    private val model: ConcatenationCanvasModelViewModel,
    private val controller: ConcatenationCanvasController
) : ChainDrawer {
    private val pointToolTip = Tooltip()
    private val contextMenu = ContextMenu()

    override fun draw() {
        val context = canvas.graphicsContext2D
        ClearDrawElement().draw(context)
        ArrowDrawElement(model.arrows, 1.0).draw(context)
        DescriptionDrawElement(model.descriptions).draw(context)

        for (cartesianSpace in model.cartesianSpaces) {
            ConcatenationFunctionDrawElement(
                cartesianSpace.functions,
                cartesianSpace.xAxis,
                cartesianSpace.yAxis
            ).draw(context)
            AxisDrawElement(
                cartesianSpace.xAxis,
                cartesianSpace.yAxis,
                model.cartesianSpaces
            ).draw(context)
        }

        TooltipsDrawElement(
            model.pointToolTipSettings,
            pointToolTip,
            model.primaryStage
        ).draw(context)

        ContextMenuDrawElement(
            contextMenu,
            model,
            controller
        ).draw(context)
    }
}