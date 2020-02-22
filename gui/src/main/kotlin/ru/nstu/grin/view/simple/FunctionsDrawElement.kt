package ru.nstu.grin.view.simple

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.model.drawable.Function
import ru.nstu.grin.model.view.SimpleCanvasViewModel
import ru.nstu.grin.view.ChainDrawElement

class FunctionsDrawElement(
    private val functions: List<Function>
) : ChainDrawElement {
    override fun draw(context: GraphicsContext) {
        for (function in functions) {
            val xPoints = function.pointArray.map { it.x }.toDoubleArray()
            val yPoints = function.pointArray.map { it.y }.toDoubleArray()
            val n = function.pointArray.size
            context.strokePolyline(xPoints, yPoints, n)
        }
    }
}