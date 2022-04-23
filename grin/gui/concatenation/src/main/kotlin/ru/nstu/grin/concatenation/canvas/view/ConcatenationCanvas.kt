package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.canvas.Canvas
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.concatenation.axis.extensions.findFunctionsAreaInsets
import ru.nstu.grin.concatenation.canvas.handlers.DraggedHandler
import ru.nstu.grin.concatenation.canvas.handlers.PressedMouseHandler
import ru.nstu.grin.concatenation.canvas.handlers.ReleaseMouseHandler
import ru.nstu.grin.concatenation.canvas.handlers.ScalableScrollHandler
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasViewModel
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel

class ConcatenationCanvas(
    private val scalableScrollHandler: ScalableScrollHandler,
    private val draggedHandler: DraggedHandler,
    private val pressedMouseHandle: PressedMouseHandler,
    private val releaseMouseHandler: ReleaseMouseHandler,
    private val model: ConcatenationCanvasModel,
    private val canvasViewModel: ConcatenationCanvasViewModel,
    private val chainDrawer: ConcatenationChainDrawer,
): Pane() {
    private val fxCoroutineScope = CoroutineScope(Dispatchers.JavaFx)

    private val functionsCanvas = Canvas(
        SettingsProvider.getCanvasWidth(),
        SettingsProvider.getCanvasHeight()
    ).apply {
        VBox.setVgrow(this, Priority.ALWAYS)
        HBox.setHgrow(this, Priority.ALWAYS)

        canvasViewModel.functionsLayerContext = graphicsContext2D
    }

    private val uiCanvas = Canvas(
        SettingsProvider.getCanvasWidth(),
        SettingsProvider.getCanvasHeight()
    ).apply {
        VBox.setVgrow(this, Priority.ALWAYS)
        HBox.setHgrow(this, Priority.ALWAYS)

        canvasViewModel.uiLayerContext = graphicsContext2D

        onScroll = scalableScrollHandler

        onMouseDragged = draggedHandler

        onMousePressed = pressedMouseHandle

        onMouseReleased = releaseMouseHandler
    }

    init {
        children.addAll(functionsCanvas, uiCanvas)

        canvasViewModel.functionsArea = model.cartesianSpaces.findFunctionsAreaInsets()

        widthProperty().addListener { _ ->
            functionsCanvas.width = width
            uiCanvas.width = width
            canvasViewModel.canvasWidth = width

            chainDrawer.draw()
        }

        heightProperty().addListener { _ ->
            functionsCanvas.height = height
            uiCanvas.height = height
            canvasViewModel.canvasHeight = height

            chainDrawer.draw()
        }

        fxCoroutineScope.launch {
            merge(
                model.axesListUpdatedEvent,
                model.functionsListUpdatedEvent,
                model.descriptionsListUpdatedEvent,
                model.cartesianSpacesListUpdatedEvent,
                model.arrowsListUpdatedEvent
            ).collectLatest {
                chainDrawer.draw()
            }
        }

        chainDrawer.draw()
    }

    fun redraw() {
        chainDrawer.draw()
    }
}