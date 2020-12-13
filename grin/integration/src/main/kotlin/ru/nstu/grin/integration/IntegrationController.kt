package ru.nstu.grin.integration

import ru.nstu.grin.concatenation.canvas.model.InitCanvasData
import ru.nstu.grin.concatenation.canvas.view.ConcatenationView
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import tornadofx.Controller
import tornadofx.Scope
import tornadofx.find

class IntegrationController : Controller() {

    fun integrate(spaces: List<CartesianSpace>) {
        val initData = InitCanvasData(
            cartesianSpaces = spaces,
            arrows = listOf(),
            descriptions = listOf()
        )
        val scope = Scope()
        find<ConcatenationView>(
            scope = scope,
            params = mapOf(
                ConcatenationView:: initData to initData
            )
        ).openWindow()
    }
}