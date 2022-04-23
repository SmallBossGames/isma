package ru.nstu.grin.concatenation.function.controller

import org.koin.core.component.get
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.WaveletFunctionFragmentModel
import ru.nstu.grin.concatenation.function.service.FunctionOperationsService
import ru.nstu.grin.concatenation.koin.MainGrinScopeWrapper
import tornadofx.Controller

class WaveletFunctionFragmentController : Controller() {
    private val mainGrinScope = find<MainGrinScopeWrapper>().koinScope

    private val functionCanvasService: FunctionOperationsService = mainGrinScope.get()

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