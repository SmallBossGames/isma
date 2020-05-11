package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.canvas.events.FunctionCopyQuery
import ru.nstu.grin.concatenation.function.model.CopyFunctionModel
import tornadofx.Controller
import java.util.*

class CopyFunctionController : Controller() {
    val functionId: UUID by param()
    private val model: CopyFunctionModel by inject()

    fun copy() {
        val event = FunctionCopyQuery(
            id = functionId,
            name = model.name
        )
        fire(event)
    }
}