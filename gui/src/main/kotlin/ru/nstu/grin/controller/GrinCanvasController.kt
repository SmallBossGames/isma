package ru.nstu.grin.controller

import ru.nstu.grin.converters.ArrowConverter
import ru.nstu.grin.converters.FunctionConverter
import ru.nstu.grin.dto.ArrowDTO
import ru.nstu.grin.dto.FunctionDTO
import ru.nstu.grin.model.GrinCanvasModel
import tornadofx.Controller
import tornadofx.FXEvent

class GrinCanvasController : Controller() {
    private val model: GrinCanvasModel by inject()

    init {
        subscribe<AddArrowEvent> {
            model.arrows.add(ArrowConverter.convert(it.arrowDto))
        }
        subscribe<AddFunctionEvent> {
            val function = FunctionConverter.merge(it.functionDTO, 0.1, 1.0)
            model.functions.add(function)
        }
    }
}

class AddArrowEvent(val arrowDto: ArrowDTO) : FXEvent()

class AddFunctionEvent(
    val functionDTO: FunctionDTO
) : FXEvent()