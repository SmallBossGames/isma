package ru.nstu.grin.controller

import ru.nstu.grin.events.concatenation.ArrowEvent
import ru.nstu.grin.converters.dto.ArrowDTOConverter
import ru.nstu.grin.model.view.ArrowViewModel
import tornadofx.Controller

class ArrowController : Controller() {
    private val model: ArrowViewModel by inject()

    fun sendArrow() {
        val arrowDTO = ArrowDTOConverter.convert(model)
        fire(ArrowEvent(arrowDTO))
    }
}