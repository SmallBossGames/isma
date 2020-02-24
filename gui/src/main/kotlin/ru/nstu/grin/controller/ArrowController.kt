package ru.nstu.grin.controller

import ru.nstu.grin.converters.dto.ArrowDTOConverter
import ru.nstu.grin.events.common.ArrowEvent
import ru.nstu.grin.events.common.ConcatenationArrowEvent
import ru.nstu.grin.events.common.SimpleArrowEvent
import ru.nstu.grin.model.CanvasType
import ru.nstu.grin.model.ConcatenationType
import ru.nstu.grin.model.SimpleType
import ru.nstu.grin.model.view.ArrowViewModel
import tornadofx.Controller

class ArrowController : Controller() {
    val type: CanvasType by param()
    private val model: ArrowViewModel by inject()

    fun sendArrow() {
        val arrow = ArrowDTOConverter.convert(model)
        when (type) {
            ConcatenationType ->
                fire(
                    ConcatenationArrowEvent(arrow)
                )
            SimpleType -> fire(
                SimpleArrowEvent(arrow)
            )
        }
    }
}