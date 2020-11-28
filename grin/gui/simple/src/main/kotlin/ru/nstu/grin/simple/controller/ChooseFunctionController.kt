package ru.nstu.grin.simple.controller

import ru.nstu.grin.common.model.ChooseFunctionWay
import ru.nstu.grin.simple.model.ChooseFunctionViewModel
import ru.nstu.grin.simple.view.modal.function.AnalyticFunctionModalView
import ru.nstu.grin.simple.view.modal.function.FileFunctionModalView
import ru.nstu.grin.simple.view.modal.function.ManualFunctionModalView
import tornadofx.Controller

class ChooseFunctionController : Controller() {
    private val model: ChooseFunctionViewModel by inject()

    fun openModal() {
        when (model.way) {
            ChooseFunctionWay.FILE -> find<FileFunctionModalView>()
            ChooseFunctionWay.INPUT -> find<ManualFunctionModalView>()
            ChooseFunctionWay.ANALYTIC -> find<AnalyticFunctionModalView>()
        }.openModal()
    }
}