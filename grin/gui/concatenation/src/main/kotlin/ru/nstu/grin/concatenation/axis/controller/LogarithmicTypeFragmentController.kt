package ru.nstu.grin.concatenation.axis.controller

import ru.nstu.grin.concatenation.axis.events.GetAxisEvent
import ru.nstu.grin.concatenation.axis.model.LogarithmicTypeModel
import tornadofx.Controller
import java.util.*

class LogarithmicTypeFragmentController : Controller() {
    private val axisId: UUID by param()
    private val model: LogarithmicTypeModel by inject()

    init {
        subscribe<GetAxisEvent> {
            if (it.axis.id == axisId) {
                model.logarithmBase = it.axis.settings.logarithmBase
                model.isOnlyIntegerPow = it.axis.settings.isOnlyIntegerPow
                model.integerStep = it.axis.settings.integerStep
            }
        }
    }
}