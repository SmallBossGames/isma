package ru.nstu.grin.concatenation.view

import javafx.collections.ListChangeListener
import javafx.scene.Parent
import javafx.scene.canvas.Canvas
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.concatenation.controller.ConcatenationCanvasController
import ru.nstu.grin.common.model.Arrow
import ru.nstu.grin.concatenation.model.CartesianSpace
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.common.model.DrawSize
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.common.utils.ColorUtils
import ru.nstu.grin.concatenation.controller.DeltaMarksGenerator
import ru.nstu.grin.concatenation.controller.DeltaSizeCalculator
import ru.nstu.grin.concatenation.draw.ConcatenationChainDrawer
import ru.nstu.grin.concatenation.dto.CartesianSpaceDTO
import ru.nstu.grin.concatenation.dto.ConcatenationAxisDTO
import ru.nstu.grin.concatenation.dto.ConcatenationFunctionDTO
import ru.nstu.grin.concatenation.events.ConcatenationFunctionEvent
import ru.nstu.grin.concatenation.model.Direction
import ru.nstu.grin.concatenation.model.ExistDirection
import ru.nstu.grin.concatenation.model.view.ConcatenationCanvasModelViewModel
import ru.nstu.grin.concatenation.view.handlers.DraggedHandler
import ru.nstu.grin.concatenation.view.handlers.ReleaseMouseHandler
import ru.nstu.grin.concatenation.view.handlers.ScalableScrollHandler
import ru.nstu.grin.concatenation.view.handlers.ShowPointHandler
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
            chainDrawer = ConcatenationChainDrawer(this, model, controller)

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

//            addFunction(
//                drawSize = DrawSize(
//                    minX = 0.0,
//                    maxX = this@canvas.width,
//                    minY = 0.0,
//                    maxY = this@canvas.height
//                )
//            )
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

        val deltaMarksGenerator = DeltaMarksGenerator()


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
                direction = ExistDirection(Direction.BOTTOM, null),
                deltaMarks = deltaMarksGenerator.getDeltaMarks(drawSize, delta, Direction.BOTTOM),
                zeroPoint = SettingsProvider.getCanvasWidth() / 2
            ),
            yAxis = ConcatenationAxisDTO(
                name = "Test",
                backGroundColor = ColorUtils.getRandomColor(),
                delimeterColor = Color.BLACK,
                direction = ExistDirection(Direction.LEFT, null),
                deltaMarks = deltaMarksGenerator.getDeltaMarks(drawSize, delta, Direction.LEFT),
                zeroPoint = SettingsProvider.getCanvasHeight() / 2
            )
        )
        val cartesianSpace2 = CartesianSpaceDTO(
            functions = listOf(function),
            xAxis = ConcatenationAxisDTO(
                name = "Test2",
                backGroundColor = ColorUtils.getRandomColor(),
                delimeterColor = Color.BLACK,
                direction = ExistDirection(Direction.BOTTOM, null),
                deltaMarks = deltaMarksGenerator.getDeltaMarks(drawSize, delta, Direction.BOTTOM),
                zeroPoint = SettingsProvider.getCanvasWidth() / 2
            ),
            yAxis = ConcatenationAxisDTO(
                name = "Test2",
                backGroundColor = ColorUtils.getRandomColor(),
                delimeterColor = Color.BLACK,
                direction = ExistDirection(Direction.LEFT, null),
                deltaMarks = deltaMarksGenerator.getDeltaMarks(drawSize, delta, Direction.LEFT),
                zeroPoint = SettingsProvider.getCanvasHeight() / 2
            )
        )
        fire(
            ConcatenationFunctionEvent(
                cartesianSpace = cartesianSpace,
                minAxisDelta = delta
            )
        )
        fire(
            ConcatenationFunctionEvent(
                cartesianSpace = cartesianSpace2,
                minAxisDelta = delta
            )
        )
    }
}