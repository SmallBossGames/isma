package ru.nstu.grin.controller

import ru.nstu.grin.model.ChooseFunctionWay
import ru.nstu.grin.model.view.ChooseFunctionViewModel
import ru.nstu.grin.view.modal.function.AbstractAddFunctionModal
import ru.nstu.grin.view.modal.function.AnalyticFunctionModalView
import ru.nstu.grin.view.modal.function.FileFunctionModalView
import ru.nstu.grin.view.modal.function.ManualEnterFunctionModalView
import tornadofx.Controller

class ChooseFunctionController : Controller() {
    private val model: ChooseFunctionViewModel by inject(params = params)

    fun getModal(): AbstractAddFunctionModal {
        return when (model.way) {
            ChooseFunctionWay.FILE -> openFunctionModal<FileFunctionModalView>()
            ChooseFunctionWay.INPUT -> openFunctionModal<ManualEnterFunctionModalView>()
            ChooseFunctionWay.ANALYTIC -> openFunctionModal<AnalyticFunctionModalView>()
        }
    }

    private inline fun <reified T : AbstractAddFunctionModal> openFunctionModal(): AbstractAddFunctionModal {
        return find<T>(
            mapOf(
                ManualEnterFunctionModalView::drawSize to model.drawSize,
                ManualEnterFunctionModalView::xExistDirections to model.xExistDirections,
                ManualEnterFunctionModalView::yExistDirections to model.yExistDirections
            )
        )
    }
}