package ru.nstu.grin.controller.simple.function

import ru.nstu.grin.controller.PointsBuilder
import ru.nstu.grin.dto.simple.SimpleFunctionDTO
import ru.nstu.grin.events.simple.SimpleFunctionEvent
import ru.nstu.grin.model.DrawSize
import ru.nstu.grin.model.simple.function.AnalyticFunctionModel
import tornadofx.Controller

class AnalyticFunctionController : Controller() {
    private val model: AnalyticFunctionModel by inject()
    private val pointsBuilder = PointsBuilder()

    fun addFunction() {
        val function = SimpleFunctionDTO(
            name = model.name,
            points = pointsBuilder.buildPoints(DrawSize(-1200.0, 1200.0, -800.0, 800.0), model.text, 0.1),
            color = model.color
        )

        fire(
            SimpleFunctionEvent(function = function)
        )
    }
}