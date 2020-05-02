package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.canvas.Canvas
import javafx.scene.control.ContextMenu
import javafx.scene.control.Tooltip
import ru.nstu.grin.common.draw.elements.ArrowDrawElement
import ru.nstu.grin.common.draw.elements.ClearDrawElement
import ru.nstu.grin.common.draw.elements.DescriptionDrawElement
import ru.nstu.grin.common.view.ChainDrawer
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.concatenation.axis.view.AxisDrawElement
import ru.nstu.grin.concatenation.function.view.ConcatenationFunctionDrawElement
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel

class ConcatenationChainDrawer(
    private val canvas: Canvas,
    private val model: ConcatenationCanvasModelViewModel,
    private val controller: ConcatenationCanvasController
) : ChainDrawer {
    private val pointToolTips = mutableListOf<Tooltip>()
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

        PointTooltipsDrawElement(
            model.pointToolTipSettings,
            pointToolTips,
            model.primaryStage
        ).draw(context)

        ContextMenuDrawElement(
            contextMenu,
            model,
            controller
        ).draw(context)
    }
}