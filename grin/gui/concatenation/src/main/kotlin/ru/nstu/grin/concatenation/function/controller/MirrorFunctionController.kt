package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import ru.nstu.grin.concatenation.function.transform.MirrorTransformer

class MirrorFunctionController(
    private val functionCanvasService: FunctionCanvasService,
) {
    fun toggleMirrorFunction(function: ConcatenationFunction, byX: Boolean = false, byY: Boolean = false){
        functionCanvasService.addOrUpdateLastTransformer<MirrorTransformer>(function){ transformer ->
            if(transformer != null){
                val mirrorX = if(byX) !transformer.mirrorX else transformer.mirrorX
                val mirrorY = if(byY) !transformer.mirrorY else transformer.mirrorY

                MirrorTransformer(mirrorX, mirrorY)
            } else {
                MirrorTransformer(byX, byY)
            }
        }
    }
}