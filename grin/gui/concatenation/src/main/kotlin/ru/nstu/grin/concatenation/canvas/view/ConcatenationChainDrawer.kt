package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.control.ContextMenu
import javafx.scene.paint.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.nstu.grin.common.draw.elements.ArrowDrawElement
import ru.nstu.grin.common.draw.elements.ClearDrawElement
import ru.nstu.grin.common.draw.elements.DescriptionDrawElement
import ru.nstu.grin.common.view.ChainDrawer
import ru.nstu.grin.concatenation.axis.view.AxisDrawElement
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformerController
import ru.nstu.grin.concatenation.canvas.model.CanvasModel
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.function.view.ConcatenationFunctionDrawElement
import ru.nstu.grin.concatenation.function.view.SpacesTransformationController
import ru.nstu.grin.concatenation.points.view.PointTooltipsDrawElement
import tornadofx.Controller
import java.util.concurrent.atomic.AtomicBoolean

class ConcatenationChainDrawer : ChainDrawer, Controller() {
    private val canvasModel: CanvasModel by inject()
    private val model: ConcatenationCanvasModel by inject()
    private val controller: ConcatenationCanvasController by inject()
    private val functionDrawElement: ConcatenationFunctionDrawElement by inject()
    private val spacesTransformationController: SpacesTransformationController by inject()
    private val pointTooltipsDrawElement: PointTooltipsDrawElement by inject()
    private val axisDrawElement: AxisDrawElement by inject()
    private val matrixTransformerController: MatrixTransformerController by inject()

    private val contextMenu = ContextMenu()

    private val drawingInProgress = AtomicBoolean(false)
    private val canvasIsDirty = AtomicBoolean(false)

    override fun draw() {
        canvasIsDirty.set(true)

        if (drawingInProgress.compareAndSet(false, true)){
            while (canvasIsDirty.compareAndSet(true, false)) {
                drawFunctionsLayer()
                drawUiLayer()
            }

            drawingInProgress.set(false)
        }
    }

    fun drawFunctionsLayer() = CoroutinesScope.launch {
        spacesTransformationController.transformSpaces()

        withContext(Dispatchers.JavaFx) {
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
    }

    fun drawUiLayer() {
        canvasModel.uiLayer.graphicsContext2D.apply {
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

    private companion object {
        val CoroutinesScope = CoroutineScope(Dispatchers.Default)
    }
}