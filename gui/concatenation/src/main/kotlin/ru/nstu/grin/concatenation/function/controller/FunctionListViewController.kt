package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.events.DeleteFunctionQuery
import ru.nstu.grin.concatenation.function.events.GetAllFunctionsEvent
import ru.nstu.grin.concatenation.function.events.GetAllFunctionsQuery
import ru.nstu.grin.concatenation.canvas.model.FunctionListViewModel
import ru.nstu.grin.concatenation.function.view.ChangeFunctionFragment
import ru.nstu.grin.concatenation.function.view.CopyFunctionFragment
import tornadofx.Controller
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

    fun openCopyModal(id: UUID) {
        find<CopyFunctionFragment>(
            mapOf(
                CopyFunctionFragment::functionId to id
            )
        ).openModal()
    }

    fun openChangeModal(id: UUID) {
        find<ChangeFunctionFragment>(
            mapOf(
                ChangeFunctionFragment::functionId to id
            )
        ).openModal()
    }

    fun getAllFunctions() {
        fire(GetAllFunctionsQuery())
    }

    fun deleteFunction(functionId: UUID) {
        val event = DeleteFunctionQuery(functionId)
        fire(event)
    }
}