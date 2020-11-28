package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.events.GetAllFunctionsEvent
import ru.nstu.grin.concatenation.function.events.GetAllFunctionsQuery
import ru.nstu.grin.concatenation.function.events.LocalizeFunctionEvent
import ru.nstu.grin.concatenation.function.model.LocalizeFunctionModel
import tornadofx.Controller
import java.util.*

class LocalizeFunctionController : Controller() {
    private val model: LocalizeFunctionModel by inject()

    init {
        subscribe<GetAllFunctionsEvent> {
            if (model.functions != null) {
                model.functions.clear()
            }
            model.functionsProperty.setAll(it.functions)
        }
    }

    fun localize(id: UUID) {
        val event = LocalizeFunctionEvent(
            id = id
        )
        fire(event)
    }

    fun getAllFunctions() {
        fire(GetAllFunctionsQuery())
    }
}