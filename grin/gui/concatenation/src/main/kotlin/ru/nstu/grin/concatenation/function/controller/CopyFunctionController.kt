package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.model.CopyFunctionModel
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService

class CopyFunctionController(
    private val functionCanvasService: FunctionCanvasService
) {
    fun copy(model: CopyFunctionModel) {
        functionCanvasService.copyFunction(model.function, model.name)
    }
}