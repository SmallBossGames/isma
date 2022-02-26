package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.events.GetFunctionEvent
import ru.nstu.grin.concatenation.function.model.UpdateFunctionData
import ru.nstu.grin.concatenation.function.model.ChangeFunctionModel
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.MirrorDetails
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import tornadofx.Controller

class ChangeFunctionController : Controller() {
    private val functionCanvasService: FunctionCanvasService by inject()
    private val model: ChangeFunctionModel by inject()

    private val function: ConcatenationFunction by param()

    init {
        subscribe<GetFunctionEvent> {
            if (it.function.id == function.id) {
                model.name = it.function.name
                model.functionColor = it.function.functionColor
                model.lineSize = it.function.lineSize.toString()
                model.lineType = it.function.lineType
                model.isHide = !it.function.isHide
                val mirrorDetails = it.function.mirrorDetails
                model.isMirrorX = mirrorDetails.isMirrorX
                model.isMirrorY = mirrorDetails.isMirrorY
            }
        }
    }

    fun updateFunction() {
        val updateFunctionData = UpdateFunctionData(
            function = function,
            name = model.name,
            color = model.functionColor,
            lineType = model.lineType,
            lineSize = model.lineSize.toDouble(),
            isHide = !model.isHide,
            mirrorDetails = MirrorDetails(isMirrorX = model.isMirrorX, isMirrorY = model.isMirrorY)
        )

        functionCanvasService.updateFunction(updateFunctionData)
    }
}