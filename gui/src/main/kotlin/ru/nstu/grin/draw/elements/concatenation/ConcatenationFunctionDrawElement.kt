package ru.nstu.grin.draw.elements.concatenation

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.model.CanvasSettings
import ru.nstu.grin.model.drawable.ConcatenationFunction
import ru.nstu.grin.model.drawable.axis.AbstractAxis
import ru.nstu.grin.settings.SettingsProvider
import ru.nstu.grin.view.ChainDrawElement

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