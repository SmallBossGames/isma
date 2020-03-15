package ru.nstu.grin.simple.draw.elements

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.simple.model.SimpleFunction
import ru.nstu.grin.simple.view.SimplePlotSettings

class FunctionsDrawElement(
    private val settings: SimplePlotSettings,
    private val functions: List<SimpleFunction>
) : ChainDrawElement {
    override fun draw(context: GraphicsContext) {
        val middleWidth = context.canvas.width / 2
        val middleHeight = context.canvas.height / 2

        for (function in functions) {
            context.stroke = function.color
            context.lineWidth = 2.0

            val points = transformPoints(middleWidth, middleHeight, function.points)

            val xPoints = points.map { it.x }.toDoubleArray()
            val yPoints = points.map { it.y }.toDoubleArray()
            val n = function.points.size
            context.strokePolyline(xPoints, yPoints, n)
        }
    }

    private fun transformPoints(zeroPointX: Double, zeroPointY: Double, points: List<Point>): List<Point> {
        return points.map {
            val x = zeroPointX + it.x * settings.pixelCost / settings.step
            val y = if (it.y > 0) {
                zeroPointY - it.y * settings.pixelCost / settings.step
            } else {
                zeroPointY + it.y * settings.pixelCost / settings.step
            }
            Point(x, y)
        }
    }
}