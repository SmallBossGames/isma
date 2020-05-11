package ru.nstu.grin.concatenation.canvas.controller

import javafx.stage.StageStyle
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.converters.model.ArrowConverter
import ru.nstu.grin.common.converters.model.DescriptionConverter
import ru.nstu.grin.common.events.ConcatenationArrowEvent
import ru.nstu.grin.common.events.ConcatenationClearCanvasEvent
import ru.nstu.grin.common.events.ConcatenationDescriptionEvent
import ru.nstu.grin.common.model.ConcatenationType
import ru.nstu.grin.common.view.modal.ArrowModalView
import ru.nstu.grin.concatenation.axis.controller.AxisCanvasController
import ru.nstu.grin.concatenation.canvas.GenerateUtils
import ru.nstu.grin.concatenation.canvas.events.*
import ru.nstu.grin.concatenation.canvas.model.CanvasModel
import ru.nstu.grin.concatenation.canvas.model.ExistDirection
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import ru.nstu.grin.concatenation.canvas.view.ConcatenationCanvas
import ru.nstu.grin.concatenation.cartesian.controller.CartesianCanvasController
import ru.nstu.grin.concatenation.description.view.DescriptionModalView
import ru.nstu.grin.concatenation.file.DrawReader
import ru.nstu.grin.concatenation.file.DrawWriter
import ru.nstu.grin.concatenation.function.controller.FunctionsCanvasController
import ru.nstu.grin.concatenation.function.events.ConcatenationFunctionEvent
import ru.nstu.grin.concatenation.function.view.AddFunctionModalView
import tornadofx.*

/**
 * Разбить по нескольким контроллерам, один для функций, другой для осей и т.д
 */
class ConcatenationCanvasController : Controller() {
    private val canvasModel: CanvasModel by inject()
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val view: ConcatenationCanvas by inject()
    private val functionsController: FunctionsCanvasController = find { }
    private val axisCanvasController: AxisCanvasController = find { }
    private val cartesianController: CartesianCanvasController = find { }

    init {
        subscribe<ConcatenationArrowEvent> { event ->
            addArrow(event)
        }
        subscribe<ConcatenationDescriptionEvent> { event ->
            addDescription(event)
        }
        subscribe<ConcatenationClearCanvasEvent> {
            clearCanvas()
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
//        addFunction()
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

    fun addArrow(event: ConcatenationArrowEvent) {
        val arrow = ArrowConverter.convert(event.arrow)
        model.arrows.add(arrow)
    }

    fun addDescription(event: ConcatenationDescriptionEvent) {
        val description = DescriptionConverter.convert(event.description)
        model.descriptions.add(description)
    }

    fun openFunctionModal(
        xExistDirection: List<ExistDirection>,
        yExistDirection: List<ExistDirection>
    ) {
        find<AddFunctionModalView>(
            mapOf(
                AddFunctionModalView::xExistDirections to xExistDirection,
                AddFunctionModalView::yExistDirections to yExistDirection
            )
        ).openModal()
    }

    fun openArrowModal(x: Double, y: Double) {
        find<ArrowModalView>(
            mapOf(
                ArrowModalView::type to ConcatenationType,
                ArrowModalView::x to x,
                ArrowModalView::y to y
            )
        ).openModal(stageStyle = StageStyle.UTILITY)
    }

    fun openDescriptionModal(x: Double, y: Double) {
        find<DescriptionModalView>(
            mapOf(
                DescriptionModalView::x to x,
                DescriptionModalView::y to y
            )
        ).openModal(stageStyle = StageStyle.UTILITY)
    }

    fun clearCanvas() {
        canvasModel.canvas.graphicsContext2D.clearRect(
            0.0, 0.0,
            SettingsProvider.getCanvasWidth(), SettingsProvider.getCanvasHeight()
        )
        model.arrows.clear()
        model.descriptions.clear()
        model.cartesianSpaces.clear()
    }
}