package ru.nstu.grin.concatenation.canvas.view

import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import ru.nstu.grin.common.draw.elements.ClearDrawElement
import ru.nstu.grin.common.view.ChainDrawer
import ru.nstu.grin.concatenation.axis.extensions.findFunctionsAreaInsets
import ru.nstu.grin.concatenation.axis.view.AxisDrawElement
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasViewModel
import ru.nstu.grin.concatenation.description.view.DescriptionDrawElement
import ru.nstu.grin.concatenation.function.view.ConcatenationFunctionDrawElement
import ru.nstu.grin.concatenation.function.view.SpacesTransformationController
import java.util.concurrent.atomic.AtomicReference

class ConcatenationChainDrawer(
    private val canvasViewModel: ConcatenationCanvasViewModel,
    private val model: ConcatenationCanvasModel,
    private val functionDrawElement: ConcatenationFunctionDrawElement,
    private val selectionDrawElement: SelectionDrawElement,
    private val spacesTransformationController: SpacesTransformationController,
    private val axisDrawElement: AxisDrawElement,
    private val descriptionDrawElement: DescriptionDrawElement,
) : ChainDrawer {
    private val coroutinesScope = CoroutineScope(Dispatchers.Default)

    private val drawingJob = AtomicReference<Job?>(null)

    override fun draw() {
        coroutinesScope.launch {
            drawingJob.getAndSet(coroutineContext.job)?.cancel()

            canvasViewModel.functionsArea = model.cartesianSpaces.findFunctionsAreaInsets()

            spacesTransformationController.transformSpaces()

            withContext(Dispatchers.JavaFx){
                drawFunctionsLayerInternal()
                drawUiLayerInternal()
            }

            drawingJob.compareAndExchange(coroutineContext.job, null)
        }
    }

    fun drawUiLayer() = coroutinesScope.launch(Dispatchers.JavaFx) {
        drawUiLayerInternal()
    }

    private fun drawFunctionsLayerInternal() {

        canvasViewModel.functionsLayerContext.apply {
            val width = canvasViewModel.canvasWidth
            val height = canvasViewModel.canvasHeight

            ClearDrawElement.draw(this, width, height)

            /*for (cartesianSpace in model.cartesianSpaces) {
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
            }*/

            functionDrawElement.draw(this, width, height)
            axisDrawElement.draw(this, width, height)
            descriptionDrawElement.draw(this, width, height)
        }
    }

    private fun drawUiLayerInternal() {
        canvasViewModel.uiLayerContext.apply {
            val width = canvasViewModel.canvasWidth
            val height = canvasViewModel.canvasHeight

            ClearDrawElement.draw(this, width, height)

            selectionDrawElement.draw(this, width, height)
        }
    }
}