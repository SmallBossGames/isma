package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.DerivativeFunctionModel
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import tornadofx.Controller

class DerivativeFunctionController : Controller() {
    private val functionCanvasService: FunctionCanvasService by inject()
    private val model: DerivativeFunctionModel by inject()
    private val function: ConcatenationFunction by param()

    fun enableDerivative() {
        functionCanvasService.derivativeFunction(function, model.derivativeType, model.degree)
    }
}