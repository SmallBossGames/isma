package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.canvas.events.FunctionQuery
import ru.nstu.grin.concatenation.canvas.events.GetFunctionEvent
import ru.nstu.grin.concatenation.canvas.events.UpdateFunctionEvent
import ru.nstu.grin.concatenation.function.model.ChangeFunctionModel
import tornadofx.Controller
import java.util.*

class ChangeFunctionController : Controller() {
    val functionId: UUID by param()
    private val model: ChangeFunctionModel by inject()

    init {
        subscribe<GetFunctionEvent> {
            if (it.function.id == functionId) {
                model.name = it.function.name
                model.functionColor = it.function.functionColor
                model.lineSize = it.function.lineSize.toString()
                model.lineType = it.function.lineType
            }
        }
        fire(FunctionQuery(functionId))
    }

    fun updateFunction() {
        val event = UpdateFunctionEvent(
            id = functionId,
            name = model.name,
            color = model.functionColor,
            lineType = model.lineType,
            lineSize = model.lineSize.toDouble()
        )
        fire(event)
    }
}