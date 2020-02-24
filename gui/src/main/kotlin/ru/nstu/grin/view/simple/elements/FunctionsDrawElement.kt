package ru.nstu.grin.view.simple.elements

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.model.drawable.SimpleFunction
import ru.nstu.grin.view.ChainDrawElement

class FunctionsDrawElement(
    private val functions: List<SimpleFunction>
) : ChainDrawElement {
    override fun draw(context: GraphicsContext) {
        for (function in functions) {
            val xPoints = function.points.map { it.x }.toDoubleArray()
            val yPoints = function.points.map { it.y }.toDoubleArray()
            val n = function.points.size
            context.strokePolyline(xPoints, yPoints, n)
        }
    }
}