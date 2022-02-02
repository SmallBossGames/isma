package ru.nstu.grin.integration

import ru.nstu.grin.concatenation.canvas.model.InitCanvasData
import ru.nstu.grin.concatenation.canvas.view.ConcatenationView
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import tornadofx.FX
import tornadofx.Scope

class GrinIntegrationFacade {
    fun integrate(spaces: List<CartesianSpace>) {
        val initData = InitCanvasData(
            cartesianSpaces = spaces,
            arrows = listOf(),
            descriptions = listOf()
        )
        val scope = Scope()
        FX.find<ConcatenationView>(
            scope = scope,
        ).apply {
            addConcatenationCanvas(initData)
        }.openWindow()
    }
}