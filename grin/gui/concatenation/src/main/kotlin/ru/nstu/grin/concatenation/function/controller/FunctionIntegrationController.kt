package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.events.CalculateIntegralEvent
import ru.nstu.grin.concatenation.function.events.GetFunctionEvent
import ru.nstu.grin.concatenation.function.model.FunctionIntegrationFragmentModel
import tornadofx.Controller
import java.util.*

class FunctionIntegrationController : Controller() {
    private val functionId: UUID by param()
    private val model: FunctionIntegrationFragmentModel by inject()


    init {
        subscribe<GetFunctionEvent> {
            if (functionId == it.function.id) {
                model.leftBorder = it.function.points.map { it.x }.min() ?: 0.0
                model.rightBorder = it.function.points.map { it.x }.max() ?: 0.0
            }
        }
    }

    fun findIntegral() {
        val event = CalculateIntegralEvent(
            functionId = functionId,
            leftBorder = model.leftBorder,
            rightBorder = model.rightBorder
        )
        fire(event)
    }
}