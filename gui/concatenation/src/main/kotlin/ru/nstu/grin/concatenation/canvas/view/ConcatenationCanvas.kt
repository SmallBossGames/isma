package ru.nstu.grin.concatenation.canvas.view

import javafx.collections.ListChangeListener
import javafx.scene.Parent
import javafx.scene.canvas.Canvas
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.common.model.Arrow
import ru.nstu.grin.concatenation.canvas.model.CartesianSpace
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.common.model.DrawSize
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.common.utils.ColorUtils
import ru.nstu.grin.concatenation.function.controller.DeltaMarksGenerator
import ru.nstu.grin.concatenation.function.controller.DeltaSizeCalculator
import ru.nstu.grin.concatenation.canvas.dto.CartesianSpaceDTO
import ru.nstu.grin.concatenation.axis.dto.ConcatenationAxisDTO
import ru.nstu.grin.concatenation.function.dto.ConcatenationFunctionDTO
import ru.nstu.grin.concatenation.function.events.ConcatenationFunctionEvent
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.model.ExistDirection
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import ru.nstu.grin.concatenation.canvas.handlers.DraggedHandler
import ru.nstu.grin.concatenation.canvas.handlers.ReleaseMouseHandler
import ru.nstu.grin.concatenation.canvas.handlers.ScalableScrollHandler
import ru.nstu.grin.concatenation.canvas.handlers.ShowPointHandler
import tornadofx.*
import kotlin.math.cos
import kotlin.math.sin

class ConcatenationCanvas : View() {
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val controller: ConcatenationCanvasController by inject()
    lateinit var canvas: Canvas
    private lateinit var chainDrawer: ConcatenationChainDrawer

    override val root: Parent = stackpane {
        println(controller.params)
        canvas(SettingsProvider.getCanvasWidth(), SettingsProvider.getCanvasHeight()) {
            vgrow = Priority.ALWAYS
            hgrow = Priority.ALWAYS
            canvas = this
            chainDrawer = ConcatenationChainDrawer(
                this,
                model,
                controller
            )

            model.arrowsProperty.addListener { _: ListChangeListener.Change<out Arrow> -> chainDrawer.draw() }
            model.cartesianSpaces.addListener { _: ListChangeListener.Change<out CartesianSpace> -> chainDrawer.draw() }
            model.descriptionsProperty.addListener { _: ListChangeListener.Change<out Description> -> chainDrawer.draw() }

            onScroll =
                ScalableScrollHandler(model, chainDrawer)

            onMouseDragged =
                DraggedHandler(model, chainDrawer)

            onMousePressed =
                ShowPointHandler(model, chainDrawer)
            onMouseReleased =
                ReleaseMouseHandler(model, chainDrawer)

            addFunction(
                drawSize = DrawSize(
                    minX = 0.0,
                    maxX = this@canvas.width,
                    minY = 0.0,
                    maxY = this@canvas.height
                )
            )
        }

    }

    private fun generateCircle(radius: Double): List<Point> {
        val result = mutableListOf<Point>()
        for (i in 0 until 360) {
            val x = radius * cos(i.toDouble())
            val y = radius * sin(i.toDouble())
            result.add(Point(x, y))
        }
        return result
    }


    fun addFunction(drawSize: DrawSize) {
        val delta = DeltaSizeCalculator().calculateDelta(drawSize)

        val deltaMarksGenerator =
            DeltaMarksGenerator()


        val function = ConcatenationFunctionDTO(
            name = "Test",
            points = generateCircle(radius = 2.0),
            functionColor = Color.BLACK
        )

        val function2 = ConcatenationFunctionDTO(
            name = "Test",
            points = generateCircle(radius = 2.0),
            functionColor = Color.BLACK
        )

        val cartesianSpace = CartesianSpaceDTO(
            functions = listOf(function),
            xAxis = ConcatenationAxisDTO(
                name = "Test",
                backGroundColor = ColorUtils.getRandomColor(),
                delimeterColor = Color.BLACK,
                direction = ExistDirection(
                    Direction.BOTTOM,
                    null
                ),
                zeroPoint = SettingsProvider.getCanvasWidth() / 2
            ),
            yAxis = ConcatenationAxisDTO(
                name = "Test",
                backGroundColor = ColorUtils.getRandomColor(),
                delimeterColor = Color.BLACK,
                direction = ExistDirection(
                    Direction.LEFT,
                    null
                ),
                zeroPoint = SettingsProvider.getCanvasHeight() / 2
            )
        )
        val cartesianSpace2 = CartesianSpaceDTO(
            functions = listOf(function),
            xAxis = ConcatenationAxisDTO(
                name = "Test2",
                backGroundColor = ColorUtils.getRandomColor(),
                delimeterColor = Color.BLACK,
                direction = ExistDirection(
                    Direction.BOTTOM,
                    null
                ),
                zeroPoint = SettingsProvider.getCanvasWidth() / 2
            ),
            yAxis = ConcatenationAxisDTO(
                name = "Test2",
                backGroundColor = ColorUtils.getRandomColor(),
                delimeterColor = Color.BLACK,
                direction = ExistDirection(
                    Direction.LEFT,
                    null
                ),
                zeroPoint = SettingsProvider.getCanvasHeight() / 2
            )
        )
        fire(
            ConcatenationFunctionEvent(
                cartesianSpace = cartesianSpace
            )
        )
        fire(
            ConcatenationFunctionEvent(
                cartesianSpace = cartesianSpace2
            )
        )
    }
}