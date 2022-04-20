package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.model.ChangeFunctionModel
import ru.nstu.grin.concatenation.function.model.UpdateFunctionData
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService

class ChangeFunctionController(
    private val functionCanvasService: FunctionCanvasService
) {
    fun updateFunction(model: ChangeFunctionModel) {
        val updateFunctionData = UpdateFunctionData(
            function = model.function,
            name = model.name,
            color = model.functionColor,
            lineType = model.lineType,
            lineSize = model.lineSize,
            isHide = !model.isHide,
        )

        functionCanvasService.updateFunction(updateFunctionData)
    }
}