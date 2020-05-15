package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.events.DerivativeFunctionEvent
import ru.nstu.grin.concatenation.function.model.DerivativeFunctionModel
import tornadofx.Controller
import java.util.*

class DerivativeFunctionController : Controller() {
    private val model: DerivativeFunctionModel by inject()
    val functionId: UUID by param()

    fun enableDerivative() {
        val event = DerivativeFunctionEvent(
            id = functionId,
            type = model.derivativeType,
            degree = model.degree
        )
        fire(event)
    }
}