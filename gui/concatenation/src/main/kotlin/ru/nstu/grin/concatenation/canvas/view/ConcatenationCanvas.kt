package ru.nstu.grin.concatenation.canvas.view

import javafx.collections.ListChangeListener
import javafx.scene.Parent
import javafx.scene.canvas.Canvas
import javafx.scene.layout.Priority
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.events.ConcatenationArrowEvent
import ru.nstu.grin.common.events.ConcatenationClearCanvasEvent
import ru.nstu.grin.common.events.ConcatenationDescriptionEvent
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.common.model.Arrow
import ru.nstu.grin.concatenation.canvas.model.CartesianSpace
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.concatenation.canvas.GenerateUtils
import ru.nstu.grin.concatenation.canvas.events.*
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import ru.nstu.grin.concatenation.canvas.handlers.DraggedHandler
import ru.nstu.grin.concatenation.canvas.handlers.ReleaseMouseHandler
import ru.nstu.grin.concatenation.canvas.handlers.ScalableScrollHandler
import ru.nstu.grin.concatenation.canvas.handlers.ShowPointHandler
import ru.nstu.grin.concatenation.file.DrawReader
import ru.nstu.grin.concatenation.file.DrawWriter
import tornadofx.*

class ConcatenationCanvas : View() {
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val controller: ConcatenationCanvasController by inject()
    lateinit var canvas: Canvas
    private lateinit var chainDrawer: ConcatenationChainDrawer

    override val root: Parent = stackpane {
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

            onScroll = ScalableScrollHandler(model, chainDrawer)

            onMouseDragged = DraggedHandler(model, chainDrawer)

            onMousePressed = ShowPointHandler(model, chainDrawer)

            onMouseReleased = ReleaseMouseHandler(model, chainDrawer)
        }
    }

    init {
        subscribe<UpdateAxisEvent> {
            controller.updateAxis(it)
        }
        subscribe<UpdateFunctionEvent> {
            controller.updateFunction(it)
        }
        subscribe<ConcatenationArrowEvent> { event ->
            controller.addArrow(event)
        }
        subscribe<ConcatenationFunctionEvent> { event ->
            controller.addConcatenationFunction(event)
        }
        subscribe<ConcatenationDescriptionEvent> { event ->
            controller.addDescription(event)
        }
        subscribe<ConcatenationClearCanvasEvent> {
            controller.clearCanvas()
        }
        subscribe<SaveEvent> {
            val writer = DrawWriter(it.file)
        }
        subscribe<LoadEvent> {
            val reader = DrawReader()
            val readResult = reader.read(it.file)
            model.arrows.addAll(readResult.arrows)
            model.descriptions.addAll(readResult.descriptions)
            TODO("Add cartesians")
        }
    }

    fun addFunction() {
        val (cartesianSpace, cartesianSpace2) = GenerateUtils.generateTwoCartesianSpaces()
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