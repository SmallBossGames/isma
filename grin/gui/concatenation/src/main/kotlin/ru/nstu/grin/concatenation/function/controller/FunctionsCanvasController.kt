package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.events.*
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import tornadofx.Controller

class FunctionsCanvasController : Controller() {
    private val service: FunctionCanvasService by inject()

    init {
        subscribe<WaveletFunctionEvent> {
            service.waveletFunction(it)
        }
        subscribe<CalculateIntegralEvent> {
            service.calculateIntegral(it)
        }
        subscribe<UpdateFunctionEvent> {
            service.updateFunction(it)
        }
        subscribe<FunctionQuery> { event ->
            val function = service.getFunction(event.id)
            fire(GetFunctionEvent(function))
        }
    }
}

