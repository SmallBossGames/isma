package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.events.GetFunctionEvent
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.FunctionIntegrationFragmentModel
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import tornadofx.Controller

class FunctionIntegrationController : Controller() {
    private val functionCanvasService: FunctionCanvasService by inject()
    private val model: FunctionIntegrationFragmentModel by inject()
    private val function: ConcatenationFunction by param()

    init {
        subscribe<GetFunctionEvent> {
            if (function.id == it.function.id) {
                model.leftBorder = it.function.points.map { it.x }.minOrNull() ?: 0.0
                model.rightBorder = it.function.points.map { it.x }.maxOrNull() ?: 0.0
            }
        }
    }

    fun findIntegral() {
        functionCanvasService.calculateIntegral(function, model.leftBorder, model.rightBorder)
    }
}