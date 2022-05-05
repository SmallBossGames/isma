package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.model.FunctionIntegrationViewModel
import ru.nstu.grin.concatenation.function.model.toModel
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService

class FunctionIntegrationController(
    private val functionCanvasService: FunctionCanvasService,
) {

    fun findIntegral(model: FunctionIntegrationViewModel) {
        functionCanvasService.addLastTransformer(model.function, model.transformerViewModel.toModel())
    }
}