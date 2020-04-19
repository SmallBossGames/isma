package ru.nstu.grin.concatenation.draw.elements

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.model.CanvasSettings
import ru.nstu.grin.concatenation.model.ConcatenationFunction
import ru.nstu.grin.concatenation.model.axis.ConcatenationAxis
import kotlin.math.abs

class ConcatenationFunctionDrawElement(
    private val functions: List<ConcatenationFunction>,
    private val xAxis: ConcatenationAxis,
    private val yAxis: ConcatenationAxis,
    private val settings: CanvasSettings
) : ChainDrawElement {
    override fun draw(context: GraphicsContext) {
        for (function in functions) {
            transformPoints(xAxis.zeroPoint, yAxis.zeroPoint, function.points)

            val points = function.points

            val xPoints = points.mapNotNull { it.xGraphic }.toDoubleArray()
            val yPoints = points.mapNotNull { it.yGraphic }.toDoubleArray()
            val n = points.size
            context.strokePolyline(
                xPoints,
                yPoints,
                n
            )
        }
    }

    private fun transformPoints(zeroPointX: Double, zeroPointY: Double, points: List<Point>) {
        for (it in points) {
            it.xGraphic = zeroPointX + it.x * settings.pixelCost / settings.step + settings.xCorrelation
            it.yGraphic = if (it.y > 0) {
                zeroPointY - it.y * settings.pixelCost / settings.step
            } else {
                zeroPointY + abs(it.y) * settings.pixelCost / settings.step
            } + settings.yCorrelation
        }
    }
}