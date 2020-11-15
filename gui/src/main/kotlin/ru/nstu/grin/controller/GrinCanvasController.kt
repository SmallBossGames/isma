package ru.nstu.grin.controller

import ru.nstu.grin.converters.ArrowConverter
import ru.nstu.grin.dto.ArrowDTO
import ru.nstu.grin.dto.FunctionDTO
import ru.nstu.grin.model.GrinCanvasModel
import tornadofx.Controller
import tornadofx.FXEvent

class GrinCanvasController : Controller() {

    private val grinCanvasModel: GrinCanvasModel by inject()

    init {
        subscribe<AddArrowEvent> {
            grinCanvasModel.arrows.add(ArrowConverter.convert(it.arrowDto))
        }
        subscribe<AddFunctionEvent> {
            println("Happen")
//            grinCanvasModel.functions.add()
//            grinCanvasModel.functions.add()
        }
    }
}

class AddArrowEvent(val arrowDto: ArrowDTO) : FXEvent()

class AddFunctionEvent(
    val functionDTO: FunctionDTO
) : FXEvent()