package ru.nstu.grin.concatenation.axis.controller

import ru.nstu.grin.concatenation.axis.events.UpdateAxisEvent
import ru.nstu.grin.concatenation.axis.service.AxisCanvasService
import tornadofx.Controller

class AxisCanvasController : Controller() {
    private val service: AxisCanvasService by inject()

    init {
        subscribe<UpdateAxisEvent> {
            service.updateAxis(it)
        }
    }
}