package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.events.GetAllFunctionsEvent
import ru.nstu.grin.concatenation.function.events.GetAllFunctionsQuery
import ru.nstu.grin.concatenation.function.events.ShowIntersectionsEvent
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.IntersectionFunctionModel
import tornadofx.Controller

class IntersectionFunctionController : Controller() {
    private val model: IntersectionFunctionModel by inject()

    init {
        subscribe<GetAllFunctionsEvent> {
            if (model.functions != null) {
                model.functions.clear()
            }
            model.functionsProperty.setAll(it.functions)
        }
    }

    fun findIntersection(list: List<ConcatenationFunction>) {
        val event = ShowIntersectionsEvent(
            id = list[0].id,
            secondId = list[1].id
        )
        fire(event)
    }

    fun getAllFunctions() {
        fire(GetAllFunctionsQuery())
    }
}