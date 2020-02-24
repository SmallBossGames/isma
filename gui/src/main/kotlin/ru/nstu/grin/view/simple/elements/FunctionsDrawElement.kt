package ru.nstu.grin.view.simple.elements

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.model.Point
import ru.nstu.grin.model.drawable.SimpleFunction
import ru.nstu.grin.view.ChainDrawElement
import ru.nstu.grin.view.simple.SimplePlotSettings

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

    private fun transformPoints(middleX: Double, middleY: Double, points: List<Point>): List<Point> {
        return points.map {
            val x = middleX + it.x * settings.xPixelCost
            val y = if (it.y > 0) {
                middleY - it.y * settings.yPixelCost
            } else {
                middleY + it.y * settings.yPixelCost
            }
            Point(x, y)
        }
    }
}