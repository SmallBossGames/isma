package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.model.CopyFunctionModel
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import tornadofx.Controller

class CopyFunctionController : Controller() {
    private val functionCanvasService: FunctionCanvasService by inject()
    private val model: CopyFunctionModel by inject(params = params)

    fun copy() {
        functionCanvasService.copyFunction(model.function, model.name)
    }
}