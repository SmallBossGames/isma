package ru.nstu.grin.concatenation.function.model

import ru.nstu.grin.concatenation.function.service.FunctionCanvasService

class FunctionWaveletViewModel(
    private val function: ConcatenationFunction,
    private val functionCanvasService: FunctionCanvasService,
) {
    val transformerViewModel = WaveletTransformerViewModel()

    fun commit() {
        functionCanvasService.addLastTransformer(function, transformerViewModel.toModel())
    }
}