package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.events.*
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import tornadofx.Controller

class FunctionsCanvasController : Controller() {
    private val service: FunctionCanvasService by inject()

    init {
        subscribe<FunctionQuery> { event ->
            val function = service.getFunction(event.id)
            fire(GetFunctionEvent(function))
        }
    }
}

