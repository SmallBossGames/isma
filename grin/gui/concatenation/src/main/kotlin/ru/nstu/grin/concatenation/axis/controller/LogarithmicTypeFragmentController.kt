package ru.nstu.grin.concatenation.axis.controller

import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.model.LogarithmicTypeModel
import tornadofx.Controller

class LogarithmicTypeFragmentController : Controller() {
    private val axis: ConcatenationAxis by param()
    private val model: LogarithmicTypeModel by inject()

    init {
        model.logarithmBase = axis.settings.logarithmBase
        model.isOnlyIntegerPow = axis.settings.isOnlyIntegerPow
        model.integerStep = axis.settings.integerStep
    }
}