package ru.nstu.grin.concatenation.function.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import org.koin.core.component.get
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.MirrorFunctionModel
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import ru.nstu.grin.concatenation.function.transform.MirrorTransformer
import ru.nstu.grin.concatenation.koin.MainGrinScopeWrapper
import tornadofx.Controller

class MirrorFunctionController : Controller() {
    private val mainGrinScope = find<MainGrinScopeWrapper>().koinScope

    private val concatenationCanvasModel: ConcatenationCanvasModel = mainGrinScope.get()
    private val functionCanvasService: FunctionCanvasService = mainGrinScope.get()

    private val coroutineScope = CoroutineScope(Dispatchers.JavaFx)
    private val model: MirrorFunctionModel by inject()

    init {
        coroutineScope.launch {
            concatenationCanvasModel.functionsListUpdatedEvent.collect{
                model.functions.setAll(it)
            }
        }

        model.functions.setAll(concatenationCanvasModel.functions)
    }

    fun mirrorFunction(isY: Boolean, function: ConcatenationFunction) {
        functionCanvasService.updateTransformer(function) { transformers ->
            val index = transformers.indexOfFirst { it is MirrorTransformer }

            if(index < 0){
                if(isY){
                    arrayOf(MirrorTransformer(mirrorY = true), *transformers)
                } else{
                    arrayOf(MirrorTransformer(mirrorX = true), *transformers)
                }
            } else{
                val newTransformers = transformers.clone()
                val mirror = newTransformers[index] as MirrorTransformer
                newTransformers[index] =
                    if(isY) mirror.copy(mirrorY = !mirror.mirrorY)
                    else mirror.copy(mirrorX = !mirror.mirrorY)

                newTransformers
            }
        }
    }

}