package ru.nstu.grin.concatenation.canvas.controller

import javafx.stage.StageStyle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nstu.grin.common.converters.model.ArrowConverter
import ru.nstu.grin.common.events.ConcatenationArrowEvent
import ru.nstu.grin.common.model.ConcatenationType
import ru.nstu.grin.common.view.modal.ArrowModalView
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.model.ExistDirection
import ru.nstu.grin.concatenation.cartesian.controller.CartesianCanvasController
import ru.nstu.grin.concatenation.description.controller.DescriptionCanvasController
import ru.nstu.grin.concatenation.description.view.DescriptionModalView
import ru.nstu.grin.concatenation.function.view.AddFunctionModalView
import tornadofx.Controller

/**
 * Разбить по нескольким контроллерам, один для функций, другой для осей и т.д
 */
class ConcatenationCanvasController : Controller() {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val model: ConcatenationCanvasModel by inject()
    private val cartesianController: CartesianCanvasController = find { }
    private val descriptionController: DescriptionCanvasController = find { }

    init {
        subscribe<ConcatenationArrowEvent> { event ->
            addArrow(event)
        }
    }

    fun addArrow(event: ConcatenationArrowEvent) {
        val arrow = ArrowConverter.convert(event.arrow)
        model.arrows.add(arrow)
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
        model.clearAll()

        coroutineScope.launch {
            model.reportUpdateAll()
        }
    }
}