package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.WaveletFunctionFragmentModel
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import tornadofx.Controller

class WaveletFunctionFragmentController : Controller() {
    private val functionCanvasService: FunctionCanvasService by inject()
    private val model: WaveletFunctionFragmentModel by inject()
    private val function: ConcatenationFunction by param()

    fun enableWavelet() {
        functionCanvasService.waveletFunction(
            function,
            model.waveletTransformFun,
            model.waveletDirection
        )
    }
}