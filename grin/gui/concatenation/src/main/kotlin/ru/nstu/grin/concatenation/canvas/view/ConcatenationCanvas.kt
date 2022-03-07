package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.Parent
import javafx.scene.layout.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import org.koin.core.component.get
import ru.isma.javafx.extensions.coroutines.flow.changeAsFlow
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.concatenation.canvas.handlers.DraggedHandler
import ru.nstu.grin.concatenation.canvas.handlers.PressedMouseHandler
import ru.nstu.grin.concatenation.canvas.handlers.ReleaseMouseHandler
import ru.nstu.grin.concatenation.canvas.handlers.ScalableScrollHandler
import ru.nstu.grin.concatenation.canvas.model.CanvasViewModel
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.koin.MainGrinScopeWrapper
import tornadofx.*


class ConcatenationCanvas : View() {
    private val fxCoroutineScope = CoroutineScope(Dispatchers.JavaFx)

    private val mainGrinScope = find<MainGrinScopeWrapper>().koinScope

    private val scalableScrollHandler = mainGrinScope.get<ScalableScrollHandler>()
    private val draggedHandler = mainGrinScope.get<DraggedHandler>()
    private val pressedMouseHandle = mainGrinScope.get<PressedMouseHandler>()

    private val model = mainGrinScope.get<ConcatenationCanvasModel>()

    private val canvasViewModel: CanvasViewModel by inject()
    private val chainDrawer: ConcatenationChainDrawer = find { }

    private val releaseMouseHandler: ReleaseMouseHandler by inject()

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

    fun redraw() {
        chainDrawer.draw()
    }
}