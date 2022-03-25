package ru.nstu.grin.concatenation.function.controller

import org.koin.core.component.get
import ru.nstu.grin.concatenation.function.model.FunctionIntegrationFragmentModel
import ru.nstu.grin.concatenation.function.service.FunctionsOperationsService
import ru.nstu.grin.concatenation.koin.MainGrinScopeWrapper
import tornadofx.Controller

class FunctionIntegrationController : Controller() {
    private val mainGrinScope = find<MainGrinScopeWrapper>().koinScope

    private val functionCanvasService: FunctionsOperationsService = mainGrinScope.get()

    fun findIntegral(model: FunctionIntegrationFragmentModel) {
        functionCanvasService.calculateIntegral(model.function, model.leftBorder, model.rightBorder)
    }
}