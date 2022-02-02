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
        drawFunctionsLayer()
        drawUiLayer()
    }

    fun drawFunctionsLayer(){
        with(canvasModel.functionsLayer.graphicsContext2D) {
            ClearDrawElement.draw(this)

            for (cartesianSpace in model.cartesianSpaces) {
                if (cartesianSpace.isShowGrid) {
                    GridDrawElement(
                        cartesianSpace.xAxis,
                        cartesianSpace.yAxis,
                        Color.valueOf("BBBBBB"),
                        matrixTransformerController
                    ).draw(this)
                    GridDrawElement(
                        cartesianSpace.xAxis,
                        cartesianSpace.yAxis,
                        Color.valueOf("EDEDED"),
                        matrixTransformerController
                    ).draw(this)
                }
            }

            functionDrawElement.draw(this)
            axisDrawElement.draw(this)
        }
    }

    fun drawUiLayer() {
        with(canvasModel.uiLayer.graphicsContext2D){
            ClearDrawElement.draw(this)

            ArrowDrawElement(model.arrows, 1.0).draw(this)
            DescriptionDrawElement(model.descriptions).draw(this)

            pointTooltipsDrawElement.draw(this)

            ContextMenuDrawElement(
                contextMenu,
                model,
                controller,
                this@ConcatenationChainDrawer,
                scope
            ).draw(this)

            SelectionDrawElement(model.selectionSettings).draw(this)
        }
    }
}