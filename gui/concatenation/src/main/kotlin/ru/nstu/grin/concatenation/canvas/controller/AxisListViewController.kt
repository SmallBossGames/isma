package ru.nstu.grin.concatenation.canvas.controller

import ru.nstu.grin.concatenation.canvas.events.GetAllAxisQuery
import ru.nstu.grin.concatenation.canvas.events.GetAllAxisesEvent
import ru.nstu.grin.concatenation.canvas.model.AxisListViewModel
import tornadofx.Controller
import tornadofx.toObservable

class AxisListViewController : Controller() {
    private val model: AxisListViewModel by inject()

    init {
        subscribe<GetAllAxisesEvent> {
            println("Get axises axisListViewController")
            model.axises = it.axises.toObservable()
        }
        fire(GetAllAxisQuery())
    }
}