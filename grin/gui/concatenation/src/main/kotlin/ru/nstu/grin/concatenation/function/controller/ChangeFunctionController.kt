package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.model.ChangeFunctionModel
import ru.nstu.grin.concatenation.function.model.MirrorDetails
import ru.nstu.grin.concatenation.function.model.UpdateFunctionData
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import tornadofx.Controller
import tornadofx.Scope

class ChangeFunctionController(
    override val scope: Scope
) : Controller() {
    private val functionCanvasService: FunctionCanvasService by inject()

    fun updateFunction(model: ChangeFunctionModel) {
        val updateFunctionData = UpdateFunctionData(
            function = model.function,
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