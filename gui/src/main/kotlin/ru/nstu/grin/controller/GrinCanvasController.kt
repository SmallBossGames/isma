package ru.nstu.grin.controller

import ru.nstu.grin.converters.ArrowConverter
import ru.nstu.grin.dto.ArrowDTO
import ru.nstu.grin.model.Arrow
import ru.nstu.grin.model.GrinCanvasModel
import tornadofx.Controller
import tornadofx.FXEvent

class GrinCanvasController : Controller() {

    private val grinCanvasModel: GrinCanvasModel by inject()

    init {
        subscribe<AddArrowEvent> {
            grinCanvasModel.arrows.add(ArrowConverter.convert(it.arrowDto))
        }
    }

}

class AddArrowEvent(val arrowDto: ArrowDTO) : FXEvent()