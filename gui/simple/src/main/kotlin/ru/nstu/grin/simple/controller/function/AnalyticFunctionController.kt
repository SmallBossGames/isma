package ru.nstu.grin.simple.controller.function

import ru.nstu.grin.common.controller.PointsBuilder
import ru.nstu.grin.common.model.DrawSize
import ru.nstu.grin.simple.dto.SimpleFunctionDTO
import ru.nstu.grin.simple.events.SimpleFunctionEvent
import ru.nstu.grin.simple.model.function.AnalyticFunctionModel
import tornadofx.Controller

class AnalyticFunctionController : Controller() {
    private val model: AnalyticFunctionModel by inject()
    private val pointsBuilder = PointsBuilder()

    fun addFunction() {
        val function = SimpleFunctionDTO(
            name = model.name,
            points = pointsBuilder.buildPoints(DrawSize(-1200.0, 1200.0, -800.0, 800.0), model.text, 0.1),
            color = model.color,
            step = model.step
        )

        fire(
            SimpleFunctionEvent(function = function)
        )
    }
}