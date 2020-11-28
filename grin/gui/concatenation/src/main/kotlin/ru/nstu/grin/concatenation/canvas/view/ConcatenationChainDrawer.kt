package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.control.ContextMenu
import javafx.scene.paint.Color
import ru.nstu.grin.common.draw.elements.ArrowDrawElement
import ru.nstu.grin.common.draw.elements.ClearDrawElement
import ru.nstu.grin.common.draw.elements.DescriptionDrawElement
import ru.nstu.grin.common.view.ChainDrawer
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.concatenation.axis.view.AxisDrawElement
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformerController
import ru.nstu.grin.concatenation.canvas.model.CanvasModel
import ru.nstu.grin.concatenation.function.view.ConcatenationFunctionDrawElement
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.points.view.PointTooltipsDrawElement
import tornadofx.Controller

class ConcatenationChainDrawer : ChainDrawer, Controller() {
    private val canvasModel: CanvasModel by inject()
    private val model: ConcatenationCanvasModel by inject()
    private val controller: ConcatenationCanvasController by inject()
    private val functionDrawElement: ConcatenationFunctionDrawElement by inject()
    private val pointTooltipsDrawElement: PointTooltipsDrawElement by inject()
    private val axisDrawElement: AxisDrawElement by inject()
    private val matrixTransformerController: MatrixTransformerController by inject()

    private val contextMenu = ContextMenu()

    override fun draw() {
        val canvas = canvasModel.canvas
        val context = canvas.graphicsContext2D
        ClearDrawElement().draw(context)
        ArrowDrawElement(model.arrows, 1.0).draw(context)
        DescriptionDrawElement(model.descriptions).draw(context)

        for (cartesianSpace in model.cartesianSpaces) {
            if (cartesianSpace.isShowGrid) {
                GridDrawElement(
                    cartesianSpace.xAxis,
                    cartesianSpace.yAxis,
                    Color.valueOf("BBBBBB"),
                    matrixTransformerController
                ).draw(context)
                GridDrawElement(
                    cartesianSpace.xAxis,
                    cartesianSpace.yAxis,
                    Color.valueOf("EDEDED"),
                    matrixTransformerController
                ).draw(context)
            }
        }

        functionDrawElement.draw(context)

        axisDrawElement.draw(context)

        pointTooltipsDrawElement.draw(context)

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