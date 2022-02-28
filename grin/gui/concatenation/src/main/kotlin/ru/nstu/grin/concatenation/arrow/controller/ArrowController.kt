package ru.nstu.grin.concatenation.arrow.controller

import ru.nstu.grin.common.converters.dto.ArrowDTOConverter
import ru.nstu.grin.common.model.CanvasType
import ru.nstu.grin.common.model.ConcatenationType
import ru.nstu.grin.common.model.SimpleType
import ru.nstu.grin.common.model.view.ArrowViewModel
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import tornadofx.Controller

class ArrowController : Controller() {
    val type: CanvasType by param()
    private val model: ArrowViewModel by inject()
    private val concatenationCanvasController: ConcatenationCanvasController by inject()

    fun sendArrow() {
        val arrow = ArrowDTOConverter.convert(model)
        when (type) {
            ConcatenationType -> {
                concatenationCanvasController.addArrow(arrow)
            }
            SimpleType ->{

            }
        }
    }
}