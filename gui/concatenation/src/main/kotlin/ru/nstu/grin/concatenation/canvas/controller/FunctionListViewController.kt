package ru.nstu.grin.concatenation.canvas.controller

import ru.nstu.grin.concatenation.canvas.events.GetAllFunctionsEvent
import ru.nstu.grin.concatenation.canvas.events.GetAllFunctionsQuery
import ru.nstu.grin.concatenation.canvas.model.FunctionListViewModel
import tornadofx.Controller
import tornadofx.asObservable

class FunctionListViewController : Controller() {
    private val model: FunctionListViewModel by inject()

    init {
        subscribe<GetAllFunctionsEvent> {
            model.functions = it.functions.asObservable()
        }
        fire(GetAllFunctionsQuery())
    }
}