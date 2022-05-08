package ru.nstu.grin.concatenation.function.model

import ru.nstu.grin.concatenation.function.service.FunctionCanvasService

class FunctionIntegrationViewModel(
    private val function: ConcatenationFunction,
    private val functionCanvasService: FunctionCanvasService,
) {
    val transformerViewModel = IntegratorTransformerViewModel()

    fun commit() {
        functionCanvasService.addLastTransformer(function, transformerViewModel.toModel())
    }
}