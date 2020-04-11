package ru.nstu.grin.concatenation.draw.elements

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.model.CanvasSettings
import ru.nstu.grin.concatenation.model.ConcatenationFunction
import ru.nstu.grin.concatenation.model.axis.ConcatenationAxis

class ConcatenationFunctionDrawElement(
    private val functions: List<ConcatenationFunction>,
    private val xAxis: ConcatenationAxis,
    private val yAxis: ConcatenationAxis,
    private val settings: CanvasSettings
) : ChainDrawElement {
    override fun draw(context: GraphicsContext) {
        for (function in functions) {
            val points = transformPoints(xAxis.zeroPoint, yAxis.zeroPoint, function.points)

            val xPoints = points.map { it.x }.toDoubleArray()
            val yPoints = points.map { it.y }.toDoubleArray()
            val n = points.size
            context.strokePolyline(
                xPoints,
                yPoints,
                n
            )
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
            Point(x + settings.xCorrelation, y + settings.yCorrelation)
        }
    }
}