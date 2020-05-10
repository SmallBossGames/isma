package ru.nstu.grin.concatenation.function.view

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformerController
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import tornadofx.Controller
import kotlin.math.log10

class ConcatenationFunctionDrawElement : ChainDrawElement, Controller() {
    private val matrixTransformer: MatrixTransformerController by inject()
    private val model: ConcatenationCanvasModelViewModel by inject()

    override fun draw(context: GraphicsContext) {
        for (cartesianSpace in model.cartesianSpaces) {
            for (function in cartesianSpace.functions) {
                context.stroke = function.functionColor
                transformPoints(function.points, cartesianSpace.xAxis, cartesianSpace.yAxis)

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
    }

    private fun transformPoints(points: List<Point>, xAxis: ConcatenationAxis, yAxis: ConcatenationAxis) {
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

            it.xGraphic = matrixTransformer.transformUnitsToPixel(x, xAxis.settings, xAxis.direction)

            val y = if (yAxis.settings.isLogarithmic) {
                if (it.y < 0) {
                    it.yGraphic = 0.0
                    continue
                }
                log10(it.y)
            } else {
                it.y
            }

            it.yGraphic = matrixTransformer.transformUnitsToPixel(y, yAxis.settings, yAxis.direction)
        }
    }
}