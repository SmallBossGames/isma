package ru.nstu.grin.concatenation.draw.elements

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.model.CanvasSettings
import ru.nstu.grin.concatenation.model.ConcatenationFunction
import ru.nstu.grin.concatenation.model.axis.AbstractAxis

class ConcatenationFunctionDrawElement(
    private val functions: List<ConcatenationFunction>,
    private val settings: CanvasSettings
) : ChainDrawElement {
    override fun draw(context: GraphicsContext) {
        for (function in functions) {
            val points = function.points

            context.strokePolyline(
                points.map { (it.x * settings.scale) + AbstractAxis.WIDTH_AXIS }.toDoubleArray(),
                points.map {
                    SettingsProvider.getCanvasHeight() - (it.y * settings.scale) - AbstractAxis.WIDTH_AXIS
                }.toDoubleArray(),
                points.size
            )
            function.xAxis.draw(context)
            function.yAxis.draw(context)
        }
    }
}