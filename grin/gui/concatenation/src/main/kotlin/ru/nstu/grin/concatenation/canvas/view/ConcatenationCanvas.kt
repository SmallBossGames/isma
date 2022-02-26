package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.Parent
import javafx.scene.layout.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.concatenation.canvas.handlers.DraggedHandler
import ru.nstu.grin.concatenation.canvas.handlers.PressedMouseHandler
import ru.nstu.grin.concatenation.canvas.handlers.ReleaseMouseHandler
import ru.nstu.grin.concatenation.canvas.handlers.ScalableScrollHandler
import ru.nstu.grin.concatenation.canvas.model.CanvasViewModel
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.model.InitCanvasData
import ru.nstu.grin.concatenation.javafx.changeAsFlow
import tornadofx.*


class ConcatenationCanvas : View() {
    private val fxCoroutineScope = CoroutineScope(Dispatchers.JavaFx)
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val model: ConcatenationCanvasModel by inject()
    private val canvasViewModel: CanvasViewModel by inject()
    private var chainDrawer: ConcatenationChainDrawer = find { }
    private val scalableScrollHandler: ScalableScrollHandler by inject()
    private val draggedHandler: DraggedHandler by inject()
    private val pressedMouseHandle: PressedMouseHandler by inject()
    private val releaseMouseHandler: ReleaseMouseHandler by inject()

    val initData: InitCanvasData? by param()

    init {
        initData?.also { data ->
            model.cartesianSpaces.setAll(data.cartesianSpaces)
            model.arrows.setAll(data.arrows)
            model.descriptions.setAll(data.descriptions)

            model.normalizeSpaces()

            coroutineScope.launch {
                model.reportUpdateAll()
            }
        }
    }

    override val root: Parent = pane {
        val c1 = canvas(SettingsProvider.getCanvasWidth(), SettingsProvider.getCanvasHeight()) {
            vgrow = Priority.ALWAYS
            hgrow = Priority.ALWAYS

            canvasViewModel.functionsLayerContext = graphicsContext2D
        }

        val c2 = canvas(SettingsProvider.getCanvasWidth(), SettingsProvider.getCanvasHeight()) {
            vgrow = Priority.ALWAYS
            hgrow = Priority.ALWAYS

            canvasViewModel.uiLayerContext = graphicsContext2D

            onScroll = scalableScrollHandler

            onMouseDragged = draggedHandler

            onMousePressed = pressedMouseHandle

            onMouseReleased = releaseMouseHandler
        }

        widthProperty().addListener { _ ->
            c1.width = width
            c2.width = width
            canvasViewModel.canvasWidth = width

            chainDrawer.draw()
        }

        heightProperty().addListener { _ ->
            c1.height = height
            c2.height = height
            canvasViewModel.canvasHeight = height

            chainDrawer.draw()
        }

        fxCoroutineScope.launch {
            launch {
                model.arrows.changeAsFlow().collect { chainDrawer.draw() }
            }
            launch {
                model.cartesianSpaces.changeAsFlow().collect { chainDrawer.draw() }
            }
            launch {
                model.descriptions.changeAsFlow().collect { chainDrawer.draw() }
            }
        }


        chainDrawer.draw()
    }

    /**
     * Cleanup the object on remove
     */
    fun dispose() {
        fxCoroutineScope.cancel()
    }

    fun redraw() {
        chainDrawer.draw()
    }
}