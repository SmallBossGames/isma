package ru.nstu.grin.controller.simple

import ru.nstu.grin.model.ChooseFunctionWay
import ru.nstu.grin.model.simple.ChooseFunctionViewModel
import ru.nstu.grin.view.simple.modal.function.AnalyticFunctionModalView
import ru.nstu.grin.view.simple.modal.function.FileFunctionModalView
import ru.nstu.grin.view.simple.modal.function.ManualFunctionModalView
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