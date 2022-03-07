package ru.nstu.grin.concatenation.function.controller

import org.koin.core.component.get
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.DerivativeFunctionModel
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import ru.nstu.grin.concatenation.koin.MainGrinScopeWrapper
import tornadofx.Controller

class DerivativeFunctionController : Controller() {
    private val mainGrinScope = find<MainGrinScopeWrapper>().koinScope

    private val functionCanvasService: FunctionCanvasService = mainGrinScope.get()

    private val model: DerivativeFunctionModel by inject()
    private val function: ConcatenationFunction by param()

    fun enableDerivative() {
        functionCanvasService.derivativeFunction(function, model.derivativeType, model.degree)
    }
}