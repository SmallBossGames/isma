package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.model.FunctionIntegrationFragmentModel
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import tornadofx.Controller

class FunctionIntegrationController : Controller() {
    private val functionCanvasService: FunctionCanvasService by inject()

    fun findIntegral(model: FunctionIntegrationFragmentModel) {
        functionCanvasService.calculateIntegral(model.function, model.leftBorder, model.rightBorder)
    }
}