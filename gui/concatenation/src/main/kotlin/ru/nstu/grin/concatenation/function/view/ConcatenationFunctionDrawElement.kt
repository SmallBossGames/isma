package ru.nstu.grin.concatenation.function.view

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.model.CartesianSpace
import kotlin.IllegalArgumentException
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.log10

class ConcatenationFunctionDrawElement(
    private val functions: List<ConcatenationFunction>,
    private val xAxis: ConcatenationAxis,
    private val yAxis: ConcatenationAxis,
    private val cartesianSpaces: List<CartesianSpace>
) : ChainDrawElement {
    override fun draw(context: GraphicsContext) {
        for (function in functions) {
            context.stroke = function.functionColor
            transformPoints(function.points)

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

    private fun transformPoints(points: List<Point>) {
        for (it in points) {
            val x = if (xAxis.settings.isLogarithmic) {
                if (it.x < 0) {
                    it.xGraphic = 0.0
                    continue
                }
                log10(it.x)
            } else {
                it.x
            }
            val minXPixel = getLeftAxisSize() * SettingsProvider.getAxisWidth()
            val maxXPixel = SettingsProvider.getCanvasWidth() - getRightAxisSize() * SettingsProvider.getAxisWidth()

            val sumPixelX = maxXPixel - minXPixel
            val minX = xAxis.settings.min
            val maxX = xAxis.settings.max
            val sumUnitsX = when {
                minX <= 0 && maxX <= 0 -> {
                    abs(minX) - abs(maxX)
                }
                minX <= 0 && maxX >= 0 -> {
                    abs(minX) + maxX
                }
                minX >= 0 && maxX >= 0 -> {
                    maxX - minX
                }
                else -> throw IllegalArgumentException("Wrong position of settings ${xAxis.settings}")
            }
            val priceUnitX = sumPixelX / sumUnitsX

            if (xAxis.settings.min < 0) {
                it.xGraphic = minXPixel + (xAxis.settings.min.absoluteValue + x) * priceUnitX
            } else {
                it.xGraphic = minXPixel + (-xAxis.settings.min.absoluteValue + x) * priceUnitX
            }

            val y = if (yAxis.settings.isLogarithmic) {
                if (it.y < 0) {
                    it.yGraphic = 0.0
                    continue
                }
                log10(it.y)
            } else {
                it.y
            }

            val minYPixel = getTopAxisSize() * SettingsProvider.getAxisWidth()
            val maxYPixel = SettingsProvider.getCanvasHeight() - getBottomAxisSize() * SettingsProvider.getAxisWidth()

            val sumPixelY = maxYPixel - minYPixel
            val minY = yAxis.settings.min
            val maxY = yAxis.settings.max
            val sumUnitsY = when {
                minY <= 0 && maxY <= 0 -> {
                    abs(minY) - abs(maxY)
                }
                minY <= 0 && maxY >= 0 -> {
                    abs(minY) + maxY
                }
                minY >= 0 && maxY >= 0 -> {
                    maxY - minY
                }
                else -> throw IllegalArgumentException("Wrong position of settings ${xAxis.settings}")
            }
            val priceUnitY = sumPixelY / sumUnitsY
            if (maxY <= 0) {
                it.yGraphic = minYPixel + (-yAxis.settings.max.absoluteValue - y) * priceUnitY
            } else {
                it.yGraphic = minYPixel + (yAxis.settings.max.absoluteValue - y) * priceUnitY
            }
        }
    }

    private fun getBottomAxisSize(): Int {
        return cartesianSpaces.filter {
            it.xAxis.direction == Direction.BOTTOM
                || it.yAxis.direction == Direction.BOTTOM
        }
            .size
    }

    private fun getTopAxisSize(): Int {
        return cartesianSpaces.filter { it.xAxis.direction == Direction.TOP || it.yAxis.direction == Direction.TOP }
            .size
    }

    private fun getLeftAxisSize(): Int {
        return cartesianSpaces.filter { it.xAxis.direction == Direction.LEFT || it.yAxis.direction == Direction.LEFT }
            .size
    }

    private fun getRightAxisSize(): Int {
        return cartesianSpaces.filter { it.xAxis.direction == Direction.RIGHT || it.yAxis.direction == Direction.RIGHT }
            .size
    }
}