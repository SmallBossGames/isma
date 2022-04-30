package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.model.FunctionWaveletViewModel
import ru.nstu.grin.concatenation.function.model.toModel
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService

class FunctionWaveletController(
    private val functionCanvasService: FunctionCanvasService,
) {
    fun applyWavelet(viewModel: FunctionWaveletViewModel) {
        functionCanvasService.addLastTransformer(viewModel.function, viewModel.transformerViewModel.toModel())
    }
}