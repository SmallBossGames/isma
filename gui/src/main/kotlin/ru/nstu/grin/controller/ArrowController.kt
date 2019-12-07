package ru.nstu.grin.controller

import ru.nstu.grin.converters.dto.ArrowDTOConverter
import ru.nstu.grin.view.model.ArrowViewModel
import tornadofx.Controller

class ArrowController : Controller() {
    private val model: ArrowViewModel by inject()

    fun sendArrow() {
        val arrowDTO = ArrowDTOConverter.convert(model)
        fire(AddArrowEvent(arrowDTO))
    }
}