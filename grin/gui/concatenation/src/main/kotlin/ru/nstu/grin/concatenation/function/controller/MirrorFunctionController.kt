package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import ru.nstu.grin.concatenation.function.transform.MirrorTransformer

class MirrorFunctionController(
    private val functionCanvasService: FunctionCanvasService,
) {
    fun toggleMirrorFunction(function: ConcatenationFunction, byX: Boolean = false, byY: Boolean = false){
        functionCanvasService.updateTransformer(function) { transformers ->
            val lastTransformer = transformers.lastOrNull()

            if (lastTransformer is MirrorTransformer){
                val mirrorX = if(byX) !lastTransformer.mirrorX else lastTransformer.mirrorX
                val mirrorY = if(byY) !lastTransformer.mirrorY else lastTransformer.mirrorY

                transformers[transformers.size - 1] = MirrorTransformer(mirrorX, mirrorY)

                transformers
            } else {
                val newArray = arrayOf(*transformers, MirrorTransformer(byX, byY))

                newArray
            }
        }
    }
}