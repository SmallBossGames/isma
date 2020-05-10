package ru.nstu.grin.concatenation.function.view

import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformerController
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import ru.nstu.grin.concatenation.function.model.LineType
import tornadofx.Controller
import kotlin.math.log10

class ConcatenationFunctionDrawElement : ChainDrawElement, Controller() {
    private val matrixTransformer: MatrixTransformerController by inject()
    private val model: ConcatenationCanvasModelViewModel by inject()

    override fun draw(context: GraphicsContext) {
        val previousLineSize = context.lineWidth
        for (cartesianSpace in model.cartesianSpaces) {
            for (function in cartesianSpace.functions) {
                if (function.isHide) continue
                context.stroke = function.functionColor
                context.fill = function.functionColor
                context.lineWidth = function.lineSize
                transformPoints(function.points, cartesianSpace.xAxis, cartesianSpace.yAxis)

                val points = function.points

                when (function.lineType) {
                    LineType.POLYNOM -> {
                        val xPoints = points.mapNotNull { it.xGraphic }.toDoubleArray()
                        val yPoints = points.mapNotNull { it.yGraphic }.toDoubleArray()
                        val n = points.size
                        context.strokePolyline(
                            xPoints,
                            yPoints,
                            n
                        )
                    }
                    LineType.RECT_FILL_DOTES -> {
                        for (point in points) {
                            val x = point.xGraphic
                            val y = point.yGraphic
                            if (x != null && y != null) {
                                context.fillRect(x, y, 2.0, 2.0)
                            }
                        }
                    }
                    LineType.SEGMENTS -> {
                        var i = 0
                        while (i < points.size - 1) {
                            val x1 = points[i].xGraphic
                            val y1 = points[i].yGraphic
                            val x2 = points[i + 1].xGraphic
                            val y2 = points[i + 1].yGraphic
                            if (x1 != null && y1 != null && x2 != null && y2 != null) {
                                context.strokeLine(
                                    x1, y1, x2, y2
                                )
                            }
                            i += 2
                        }
                    }
                    LineType.RECT_UNFIL_DOTES -> {
                        for (point in points) {
                            val x = point.xGraphic
                            val y = point.yGraphic
                            if (x != null && y != null) {
                                context.strokeRect(x, y, 1.0, 1.0)
                            }
                        }
                    }
                    LineType.CIRCLE_FILL_DOTES -> {
                        for (point in points) {
                            val x = point.xGraphic
                            val y = point.yGraphic
                            if (x != null && y != null) {
                                context.fillOval(x, y, 2.0, 2.0)
                            }
                        }
                    }
                    LineType.CIRCLE_UNFILL_DOTES -> {
                        for (point in points) {
                            val x = point.xGraphic
                            val y = point.yGraphic
                            if (x != null && y != null) {
                                context.strokeOval(x, y, 1.0, 1.0)
                            }
                        }
                    }
                }
            }
        }
        context.lineWidth = previousLineSize
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