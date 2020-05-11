package ru.nstu.grin.concatenation.canvas.controller

import ru.nstu.grin.concatenation.canvas.events.DeleteFunctionQuery
import ru.nstu.grin.concatenation.canvas.events.GetAllFunctionsEvent
import ru.nstu.grin.concatenation.canvas.events.GetAllFunctionsQuery
import ru.nstu.grin.concatenation.canvas.model.FunctionListViewModel
import ru.nstu.grin.concatenation.function.view.ChangeFunctionFragment
import tornadofx.Controller
import tornadofx.asObservable
import java.util.*

class FunctionListViewController : Controller() {
    private val model: FunctionListViewModel by inject()

    init {
        subscribe<GetAllFunctionsEvent> {
            if (model.functions != null) {
                model.functions.clear()
            }
            model.functionsProperty.setAll(it.functions)
        }
    }

    fun openChangeModal(id: UUID) {
        find<ChangeFunctionFragment>(
            mapOf(
                ChangeFunctionFragment::functionId to id
            )
        ).openWindow()
    }

    fun getAllFunctions() {
        fire(GetAllFunctionsQuery())
    }

    fun deleteFunction(functionId: UUID) {
        val event = DeleteFunctionQuery(functionId)
        fire(event)
    }
}