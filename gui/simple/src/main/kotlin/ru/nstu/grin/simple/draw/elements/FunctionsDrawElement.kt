package ru.nstu.grin.simple.draw.elements

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.simple.model.SimpleFunction
import ru.nstu.grin.simple.view.SimplePlotSettings
import kotlin.math.abs
import kotlin.math.log
import kotlin.math.log10

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

            transformPoints(middleWidth, middleHeight, function.points)
            val points = function.points

            val xPoints = points.mapNotNull { it.xGraphic }.toDoubleArray()
            val yPoints = points.mapNotNull { it.yGraphic }.toDoubleArray()
            val n = function.points.size
            context.strokePolyline(xPoints, yPoints, n)
        }
    }

    private fun transformPoints(zeroPointX: Double, zeroPointY: Double, points: List<Point>) {
        for (it in points) {
            val x = if (settings.isXLogarithmic) {
                if (it.x < 0) {
                    it.xGraphic = 0.0
                    continue
                }
                log10(it.x)
            } else {
                it.x
            }
            it.xGraphic = zeroPointX + x * settings.pixelCost / settings.step + settings.xCorrelation

            val y = if (settings.isYLogarithmic) {
                if (it.y < 0) {
                    it.yGraphic = 0.0
                    continue
                }
                log10(it.y)
            } else {
                it.y
            }
            it.yGraphic = if (y > 0) {
                zeroPointY - y * settings.pixelCost / settings.step
            } else {
                zeroPointY + abs(y) * settings.pixelCost / settings.step
            } + settings.yCorrelation
        }
    }
}