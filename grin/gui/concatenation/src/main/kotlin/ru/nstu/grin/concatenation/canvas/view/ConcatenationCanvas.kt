package ru.nstu.grin.concatenation.canvas.view

import javafx.collections.ListChangeListener
import javafx.scene.Parent
import javafx.scene.layout.Priority
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.model.Arrow
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.concatenation.canvas.handlers.DraggedHandler
import ru.nstu.grin.concatenation.canvas.handlers.PressedMouseHandler
import ru.nstu.grin.concatenation.canvas.handlers.ReleaseMouseHandler
import ru.nstu.grin.concatenation.canvas.handlers.ScalableScrollHandler
import ru.nstu.grin.concatenation.canvas.model.CanvasModel
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.model.InitCanvasData
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import tornadofx.*

class ConcatenationCanvas : View() {
    private val model: ConcatenationCanvasModel by inject()
    private val canvasModel: CanvasModel by inject()
    private var chainDrawer: ConcatenationChainDrawer = find { }
    private val scalableScrollHandler: ScalableScrollHandler by inject()
    private val draggedHandler: DraggedHandler by inject()
    private val pressedMouseHandle: PressedMouseHandler by inject()
    private val releaseMouseHandler: ReleaseMouseHandler by inject()

    val initData: InitCanvasData? by param()

    init {
        initData?.also { data ->
            model.cartesianSpaces = data.cartesianSpaces.toObservable()
            model.arrows = data.arrows.toObservable()
            model.descriptions = data.descriptions.toObservable()

            model.normalizeSpaces()
        }
    }

    override val root: Parent = pane {
        canvas(SettingsProvider.getCanvasWidth(), SettingsProvider.getCanvasHeight()) {
            vgrow = Priority.ALWAYS
            hgrow = Priority.ALWAYS

            canvasModel.functionsLayer = this

            chainDrawer.drawFunctionsLayer()
        }

        canvas(SettingsProvider.getCanvasWidth(), SettingsProvider.getCanvasHeight()) {
            vgrow = Priority.ALWAYS
            hgrow = Priority.ALWAYS

            canvasModel.uiLayer = this

            model.arrowsProperty.addListener { _: ListChangeListener.Change<out Arrow> -> chainDrawer.draw() }
            model.cartesianSpaces.addListener { _: ListChangeListener.Change<out CartesianSpace> -> chainDrawer.draw() }
            model.descriptionsProperty.addListener { _: ListChangeListener.Change<out Description> -> chainDrawer.draw() }

            onScroll = scalableScrollHandler

            onMouseDragged = draggedHandler

            onMousePressed = pressedMouseHandle

            onMouseReleased = releaseMouseHandler

            chainDrawer.drawUiLayer()
        }
    }

    fun redraw() {
        chainDrawer.draw()
    }
}