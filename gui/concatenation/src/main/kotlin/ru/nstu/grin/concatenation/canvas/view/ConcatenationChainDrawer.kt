package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.canvas.Canvas
import javafx.scene.control.ContextMenu
import javafx.scene.control.Tooltip
import javafx.scene.paint.Color
import ru.nstu.grin.common.draw.elements.*
import ru.nstu.grin.common.view.ChainDrawer
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.concatenation.axis.view.AxisDrawElement
import ru.nstu.grin.concatenation.canvas.model.CanvasModel
import ru.nstu.grin.concatenation.function.view.ConcatenationFunctionDrawElement
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import tornadofx.Controller

class ConcatenationChainDrawer : ChainDrawer, Controller() {
    private val canvasModel: CanvasModel by inject()
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val controller: ConcatenationCanvasController by inject()

    private val pointToolTips = mutableListOf<Tooltip>()
    private val contextMenu = ContextMenu()

    override fun draw() {
        val canvas = canvasModel.canvas
        val context = canvas.graphicsContext2D
        ClearDrawElement().draw(context)
        ArrowDrawElement(model.arrows, 1.0).draw(context)
        DescriptionDrawElement(model.descriptions).draw(context)

        for (cartesianSpace in model.cartesianSpaces) {
            if (cartesianSpace.isShowGrid) {
                val pixelCost = cartesianSpace.xAxis.settings.pixelCost
                GridDrawElement(
                    pixelCost / 5,
                    Color.valueOf("EDEDED"),
                    cartesianSpace.xAxis.settings.correlation,
                    cartesianSpace.yAxis.settings.correlation,
                    true
                ).draw(context)
                GridDrawElement(
                    pixelCost,
                    Color.valueOf("BBBBBB"),
                    cartesianSpace.xAxis.settings.correlation,
                    cartesianSpace.yAxis.settings.correlation,
                    true
                ).draw(context)
            }
        }

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
            controller,
            this,
            scope
        ).draw(context)

        SelectionDrawElement(model.selectionSettings).draw(context)
    }
}