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
        functionCanvasService.updateTransformer(function) { transformers ->
            val lastTransformer = transformers.lastOrNull()

            if (lastTransformer is DerivativeTransformer){
                transformers[transformers.size - 1] = lastTransformer.copy(
                    degree = lastTransformer.degree + 1
                )

                transformers
            } else {
                val transformer = DerivativeTransformer(
                    degree = 1,
                    type = DerivativeType.LEFT,
                    axis = DerivativeAxis.Y_BY_X
                )

                val newArray = arrayOf(*transformers, transformer)

                newArray
            }
        }
    }
}