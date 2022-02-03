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
import kotlin.math.max
import kotlin.math.min

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

            model.cartesianSpaces.forEach { space ->
                var minX = Double.POSITIVE_INFINITY
                var maxX = Double.NEGATIVE_INFINITY

                var minY = Double.POSITIVE_INFINITY
                var maxY = Double.NEGATIVE_INFINITY

                space.functions.forEach{ function ->
                    function.points.forEach { point ->
                        minX = min(point.x, minX)
                        maxX = max(point.x, maxX)

                        minY = min(point.y, minY)
                        maxY = max(point.y, maxY)
                    }
                }

                val indentX = (maxX - minX) * DEFAULT_INDENT
                val indentY = (maxY - minY) * DEFAULT_INDENT

                space.xAxis.settings.min = minX - indentX
                space.xAxis.settings.max = maxX + indentX

                space.yAxis.settings.min = minY - indentY
                space.yAxis.settings.max = maxY + indentY
            }
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

    private companion object {
        const val DEFAULT_INDENT = 0.01
    }
}