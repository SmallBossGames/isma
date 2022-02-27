package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.model.CopyFunctionModel
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import tornadofx.Controller
import tornadofx.Scope

class CopyFunctionController(
    override val scope: Scope
) : Controller() {
    private val functionCanvasService: FunctionCanvasService by inject()

    fun copy(model: CopyFunctionModel) {
        functionCanvasService.copyFunction(model.function, model.name)
    }
}