package ru.nstu.grin.controller

import javafx.stage.StageStyle
import ru.nstu.grin.converters.model.ArrowConverter
import ru.nstu.grin.converters.model.DescriptionConverter
import ru.nstu.grin.converters.model.FunctionConverter
import ru.nstu.grin.dto.ArrowDTO
import ru.nstu.grin.dto.DescriptionDTO
import ru.nstu.grin.dto.FunctionDTO
import ru.nstu.grin.model.view.GrinCanvasModelViewModel
import ru.nstu.grin.view.modal.ArrowModalView
import ru.nstu.grin.view.modal.ChooseFunctionModalView
import ru.nstu.grin.view.modal.DescriptionModalView
import tornadofx.*
import tornadofx.FXEvent

class GrinCanvasController : Controller() {
    private val model: GrinCanvasModelViewModel by inject()

    init {
        subscribe<AddArrowEvent> {
            val arrow = ArrowConverter.convert(it.arrowDTO)
            model.arrows.add(arrow)
        }
        subscribe<AddFunctionEvent> {
            val function = FunctionConverter.merge(it.functionDTO, 0.1, 1.0)
            model.functions.add(function)
        }
        subscribe<AddDescriptionEvent> {
            val description = DescriptionConverter.convert(it.descriptionDTO)
            model.descriptions.add(description)
        }
    }

    fun openFunctionModal() {
        find<ChooseFunctionModalView>().openModal()
    }

    fun openArrowModal(x: Double, y: Double) {
        find<ArrowModalView>(
            mapOf(
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
}

class AddArrowEvent(val arrowDTO: ArrowDTO) : FXEvent()

class AddFunctionEvent(
    val functionDTO: FunctionDTO
) : FXEvent()

class AddDescriptionEvent(val descriptionDTO: DescriptionDTO) : FXEvent()