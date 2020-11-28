package ru.nstu.grin.common.controller

import ru.nstu.grin.common.converters.dto.ArrowDTOConverter
import ru.nstu.grin.common.events.ConcatenationArrowEvent
import ru.nstu.grin.common.events.SimpleArrowEvent
import ru.nstu.grin.common.model.CanvasType
import ru.nstu.grin.common.model.ConcatenationType
import ru.nstu.grin.common.model.SimpleType
import ru.nstu.grin.common.model.view.ArrowViewModel
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