package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.DerivativeTransformerViewModel
import ru.nstu.grin.concatenation.function.service.FunctionOperationsService

class DerivativeFunctionController(
    private val function: ConcatenationFunction,
    private val functionCanvasService: FunctionOperationsService
){
    //TODO: disabled until migration to Async Transformers
    fun enableDerivative(model: DerivativeTransformerViewModel) {
        functionCanvasService.derivativeFunction(function, model.type, model.degree)
    }
}