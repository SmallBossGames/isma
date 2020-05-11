package ru.nstu.grin.concatenation.canvas.controller

import ru.nstu.grin.concatenation.canvas.events.*
import ru.nstu.grin.concatenation.canvas.service.AxisCanvasService
import tornadofx.Controller

class AxisCanvasController : Controller() {
    private val service: AxisCanvasService by inject()

    init {
        subscribe<UpdateAxisEvent> {
            service.updateAxis(it)
        }
        subscribe<AxisQuery> {
            service.getAxis(it.id)
        }
        subscribe<GetAllAxisQuery> {
            service.getAllAxises()
        }
    }
}