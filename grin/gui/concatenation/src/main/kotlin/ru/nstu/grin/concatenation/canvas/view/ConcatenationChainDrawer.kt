package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.paint.Color
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import ru.nstu.grin.common.draw.elements.ArrowDrawElement
import ru.nstu.grin.common.draw.elements.ClearDrawElement
import ru.nstu.grin.common.draw.elements.DescriptionDrawElement
import ru.nstu.grin.common.view.ChainDrawer
import ru.nstu.grin.concatenation.axis.view.AxisDrawElement
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformer
import ru.nstu.grin.concatenation.canvas.model.CanvasViewModel
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.function.view.ConcatenationFunctionDrawElement
import ru.nstu.grin.concatenation.function.view.SpacesTransformationController
import ru.nstu.grin.concatenation.points.view.PointTooltipsDrawElement
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

class ConcatenationChainDrawer(
    private val canvasViewModel: CanvasViewModel,
    private val model: ConcatenationCanvasModel,
    private val functionDrawElement: ConcatenationFunctionDrawElement,
    private val selectionDrawElement: SelectionDrawElement,
    private val pointTooltipsDrawElement: PointTooltipsDrawElement,
    private val spacesTransformationController: SpacesTransformationController,
    private val axisDrawElement: AxisDrawElement,
    private val arrowDrawElement: ArrowDrawElement,
    private val descriptionDrawElement: DescriptionDrawElement,
    private val matrixTransformer: MatrixTransformer,
) : ChainDrawer {
    private val drawingInProgress = AtomicBoolean(false)
    private val transformationJob = AtomicReference<Job>(null)

    override fun draw() {
        val transformationJobLocal = transformationJob.getAndSet(
            CoroutinesScope.launch {
                spacesTransformationController.transformSpaces()
            }
        )

        transformationJobLocal?.cancel()

        if (drawingInProgress.compareAndSet(false, true)) {
            CoroutinesScope.launch {
                while (transformationJob.getAndSet(null)?.join() != null){
                    drawFunctionsLayer()
                    drawUiLayer()
                }

                drawingInProgress.set(false)
            }
        }
    }

    suspend fun drawFunctionsLayer() = withContext(Dispatchers.JavaFx) {
        canvasViewModel.functionsLayerContext.apply {
            val width = canvasViewModel.canvasWidth
            val height = canvasViewModel.canvasHeight

            ClearDrawElement.draw(this, width, height)

            for (cartesianSpace in model.cartesianSpaces) {
                if (cartesianSpace.isShowGrid) {
                    GridDrawElement(
                        cartesianSpace.xAxis,
                        cartesianSpace.yAxis,
                        Color.valueOf("BBBBBB"),
                        matrixTransformer
                    ).draw(this, width, height)
                    GridDrawElement(
                        cartesianSpace.xAxis,
                        cartesianSpace.yAxis,
                        Color.valueOf("EDEDED"),
                        matrixTransformer
                    ).draw(this, width, height)
                }
            }

            functionDrawElement.draw(this, width, height)
            axisDrawElement.draw(this, width, height)
        }
    }


    suspend fun drawUiLayer() = withContext(Dispatchers.JavaFx) {
        canvasViewModel.uiLayerContext.apply {
            val width = canvasViewModel.canvasWidth
            val height = canvasViewModel.canvasHeight

            ClearDrawElement.draw(this, width, height)

            arrowDrawElement.draw(this, width, height)
            descriptionDrawElement.draw(this, width, height)
            pointTooltipsDrawElement.draw(this, width, height)
            selectionDrawElement.draw(this, width, height)
        }
    }

    private companion object {
        val CoroutinesScope = CoroutineScope(Dispatchers.Default)
    }
}