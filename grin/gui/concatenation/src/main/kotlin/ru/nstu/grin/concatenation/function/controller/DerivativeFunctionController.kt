package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import ru.nstu.grin.concatenation.function.transform.DerivativeAxis
import ru.nstu.grin.concatenation.function.transform.DerivativeTransformer
import ru.nstu.grin.concatenation.function.transform.DerivativeType

class DerivativeFunctionController(
    private val functionCanvasService: FunctionCanvasService
){
    fun applyDerivative(function: ConcatenationFunction) {
        functionCanvasService.addOrUpdateLastTransformer<DerivativeTransformer>(function){ transformer ->
            transformer
                ?.copy(degree = transformer.degree + 1)
                ?: DerivativeTransformer(1, DerivativeType.LEFT, DerivativeAxis.Y_BY_X)
        }
    }
}