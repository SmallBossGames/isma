package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.canvas.events.FunctionQuery
import ru.nstu.grin.concatenation.canvas.events.GetFunctionEvent
import ru.nstu.grin.concatenation.function.model.ChangeFunctionModel
import tornadofx.Controller
import java.util.*

class ChangeFunctionController: Controller() {
    val functionId: UUID by param()
    private val model: ChangeFunctionModel by inject()

    init {
        subscribe<GetFunctionEvent> {

        }
        fire(FunctionQuery(functionId))
    }
}