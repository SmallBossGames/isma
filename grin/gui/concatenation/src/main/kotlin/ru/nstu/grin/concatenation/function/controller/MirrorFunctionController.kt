package ru.nstu.grin.concatenation.function.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.function.model.UpdateFunctionData
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.MirrorFunctionModel
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import tornadofx.Controller

class MirrorFunctionController : Controller() {
    private val functionCanvasService: FunctionCanvasService by inject()
    private val concatenationCanvasModel: ConcatenationCanvasModel by inject()
    private val coroutineScope = CoroutineScope(Dispatchers.JavaFx)
    private val model: MirrorFunctionModel by inject()

    init {
        coroutineScope.launch {
            concatenationCanvasModel.functionsListUpdatedEvent.collect{
                model.functions.setAll(it)
            }
        }

        model.functions.setAll(concatenationCanvasModel.getAllFunctions())
    }

    fun mirrorFunction(isY: Boolean, function: ConcatenationFunction) {
        val mirrorDetails = function.mirrorDetails
        val updateFunctionData = UpdateFunctionData(
            function = function,
            name = function.name,
            color = function.functionColor,
            lineType = function.lineType,
            lineSize = function.lineSize,
            isHide = function.isHide,
            mirrorDetails = if (isY) {
                mirrorDetails.copy(
                    isMirrorY = !mirrorDetails.isMirrorY
                )
            } else {
                mirrorDetails.copy(
                    isMirrorX = !mirrorDetails.isMirrorX
                )
            }
        )

        functionCanvasService.updateFunction(updateFunctionData)
    }

}