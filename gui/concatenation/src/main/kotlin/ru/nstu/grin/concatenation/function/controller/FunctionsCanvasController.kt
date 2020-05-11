package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.events.*
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import tornadofx.Controller

class FunctionsCanvasController : Controller() {
    private val service: FunctionCanvasService by inject()

    init {
        subscribe<ConcatenationFunctionEvent> { event ->
            service.addFunction(event)
        }
        subscribe<FunctionCopyQuery> {
            service.copyFunction(it)
        }
        subscribe<UpdateFunctionEvent> {
            service.updateFunction(it)
        }
        subscribe<FunctionQuery> { event ->
            service.getFunction(event)
        }
        subscribe<GetAllFunctionsQuery> {
            service.getAllFunctions()
        }
        subscribe<DeleteFunctionQuery> {
            service.deleteFunction(it)
        }
    }
}