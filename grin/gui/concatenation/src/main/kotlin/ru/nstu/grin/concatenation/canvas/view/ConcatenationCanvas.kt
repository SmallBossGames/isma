package ru.nstu.grin.concatenation.canvas.view

import javafx.collections.ListChangeListener
import javafx.scene.Parent
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.common.model.Arrow
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.handlers.DraggedHandler
import ru.nstu.grin.concatenation.canvas.handlers.ReleaseMouseHandler
import ru.nstu.grin.concatenation.canvas.handlers.ScalableScrollHandler
import ru.nstu.grin.concatenation.canvas.handlers.PressedMouseHandler
import ru.nstu.grin.concatenation.canvas.model.CanvasModel
import ru.nstu.grin.concatenation.canvas.model.InitCanvasData
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.LineType
import tornadofx.*
import java.util.*

class ConcatenationCanvas : View() {
    private val model: ConcatenationCanvasModel by inject()
    private val controller: ConcatenationCanvasController = find { }
    private val canvasModel: CanvasModel by inject()
    private var chainDrawer: ConcatenationChainDrawer = find { }
    private val scalableScrollHandler: ScalableScrollHandler by inject()
    private val draggedHandler: DraggedHandler by inject()
    private val pressedMouseHandle: PressedMouseHandler by inject()
    private val releaseMouseHandler: ReleaseMouseHandler by inject()

    val initData: InitCanvasData? by param()

    init {
        val data = InitCanvasData(
            cartesianSpaces = listOf(
                CartesianSpace(
                    id = UUID.randomUUID(),
                    name = "tt",
                    functions = mutableListOf(
                        ConcatenationFunction(
                            id = UUID.randomUUID(),
                            name = "SomeNmae",
                            points = listOf(Point(0.5, 0.5), Point(5.0, 5.0), Point(9.0, 3.0)),
                            isHide = false,
                            isSelected = false,
                            functionColor = Color.BLUE,
                            lineSize = 5.0,
                            lineType = LineType.POLYNOM
                        )
                    ),
                    xAxis = ConcatenationAxis(
                        id = UUID.randomUUID(),
                        name = "tt",
                        order = 0,
                        direction = Direction.LEFT,
                        backGroundColor = Color.BLACK,
                        fontColor = Color.CYAN,
                        distanceBetweenMarks = 40.0,
                        textSize = 12.0,
                        font = "Arial"
                    ),
                    yAxis = ConcatenationAxis(
                        id = UUID.randomUUID(),
                        name = "tt",
                        order = 0,
                        direction = Direction.BOTTOM,
                        backGroundColor = Color.BLACK,
                        fontColor = Color.CYAN,
                        distanceBetweenMarks = 40.0,
                        textSize = 12.0,
                        font = "Arial"
                    )
                )
            ),
            listOf(),
            listOf()
        )

        if (data != null) {
            model.cartesianSpaces = data.cartesianSpaces.toObservable()
            model.arrows = data.arrows.toObservable()
            model.descriptions = data.descriptions.toObservable()
        }
    }

    override val root: Parent = stackpane {
        canvas(SettingsProvider.getCanvasWidth(), SettingsProvider.getCanvasHeight()) {
            vgrow = Priority.ALWAYS
            hgrow = Priority.ALWAYS
            canvasModel.canvas = this

            model.arrowsProperty.addListener { _: ListChangeListener.Change<out Arrow> -> chainDrawer.draw() }
            model.cartesianSpaces.addListener { _: ListChangeListener.Change<out CartesianSpace> -> chainDrawer.draw() }
            model.descriptionsProperty.addListener { _: ListChangeListener.Change<out Description> -> chainDrawer.draw() }

            onScroll = scalableScrollHandler

            onMouseDragged = draggedHandler

            onMousePressed = pressedMouseHandle

            onMouseReleased = releaseMouseHandler

            chainDrawer.draw()
        }
    }

    fun redraw() {
        chainDrawer.draw()
    }
}