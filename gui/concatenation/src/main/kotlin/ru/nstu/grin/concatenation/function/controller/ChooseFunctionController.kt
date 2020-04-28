package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.common.model.ChooseFunctionWay
import ru.nstu.grin.concatenation.function.model.ChooseFunctionViewModel
import ru.nstu.grin.concatenation.function.view.AbstractAddFunctionModal
import ru.nstu.grin.concatenation.function.view.AnalyticFunctionModalView
import ru.nstu.grin.concatenation.function.view.FileFunctionModalView
import ru.nstu.grin.concatenation.function.view.ManualEnterFunctionModalView
import tornadofx.Controller

class ChooseFunctionController : Controller() {
    private val model: ChooseFunctionViewModel by inject(params = params)

    fun openModal() {
        when (model.way) {
            ChooseFunctionWay.FILE -> openFunctionModal<FileFunctionModalView>()
            ChooseFunctionWay.INPUT -> openFunctionModal<ManualEnterFunctionModalView>()
            ChooseFunctionWay.ANALYTIC -> openFunctionModal<AnalyticFunctionModalView>()
        }.openModal()
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