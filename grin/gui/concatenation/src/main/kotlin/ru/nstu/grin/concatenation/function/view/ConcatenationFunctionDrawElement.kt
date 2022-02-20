package ru.nstu.grin.concatenation.function.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.function.model.LineType
import tornadofx.Controller

class ConcatenationFunctionDrawElement : ChainDrawElement, Controller() {
    private val model: ConcatenationCanvasModel by inject()

    override fun draw(context: GraphicsContext, canvasWidth: Double, canvasHeight: Double) {
        val previousLineSize = context.lineWidth
        for (cartesianSpace in model.cartesianSpaces) {
            for (function in cartesianSpace.functions) {
                if (function.isHide) continue

                context.stroke = function.functionColor
                context.fill = function.functionColor
                context.lineWidth = function.lineSize

                val points = function.points

                val xPoints = points.mapNotNull { it.xGraphic }.toDoubleArray()
                val yPoints = points.mapNotNull { it.yGraphic }.toDoubleArray()

                val n = if (function.derivativeDetails != null) {
                    xPoints.size - 2
                } else {
                    xPoints.size
                }

                if (function.isSelected) {
                    context.fill = Color.RED
                    context.stroke = Color.RED
                }

                when (function.lineType) {
                    LineType.POLYNOM -> {
                        context.strokePolyline(
                            xPoints,
                            yPoints,
                            n
                        )
                    }
                    LineType.RECT_FILL_DOTES -> {
                        for (i in 0 until n) {
                            val x = points[i].xGraphic
                            val y = points[i].yGraphic
                            if (x != null && y != null) {
                                context.fillRect(x, y, 2.0, 2.0)
                            }
                        }
                    }
                    LineType.SEGMENTS -> {
                        var i = 0
                        while (i < n - 1) {
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
                        for (i in 0 until n) {
                            val x = points[i].xGraphic
                            val y = points[i].yGraphic
                            if (x != null && y != null) {
                                context.strokeRect(x, y, 1.0, 1.0)
                            }
                        }
                    }
                    LineType.CIRCLE_FILL_DOTES -> {
                        for (i in 0 until n) {
                            val x = points[i].xGraphic
                            val y = points[i].yGraphic
                            if (x != null && y != null) {
                                context.fillOval(x, y, 2.0, 2.0)
                            }
                        }
                    }
                    LineType.CIRCLE_UNFILL_DOTES -> {
                        for (i in 0 until n) {
                            val x = points[i].xGraphic
                            val y = points[i].yGraphic
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
}