package ru.nstu.grin.concatenation.function.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasViewModel
import ru.nstu.grin.concatenation.function.model.LineType

class ConcatenationFunctionDrawElement(
    private val model: ConcatenationCanvasModel,
    private val canvasViewModel: ConcatenationCanvasViewModel,
) : ChainDrawElement {

    override fun draw(context: GraphicsContext, canvasWidth: Double, canvasHeight: Double) {
        val previousLineSize = context.lineWidth
        for (cartesianSpace in model.cartesianSpaces) {
            for (function in cartesianSpace.functions) {
                val points = function.pixelsToDraw ?: continue

                context.stroke = function.functionColor
                context.fill = function.functionColor
                context.lineWidth = function.lineSize

                val xPoints = points.first
                val yPoints = points.second

                val n = xPoints.size

                //TODO: disabled until migration to Async Transformers
               /* val n = if (function.derivativeDetails != null) {
                    xPoints.size - 2
                } else {
                    xPoints.size
                }*/

                if (canvasViewModel.selectedFunctions.contains(function)) {
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
                            val x = xPoints[i]
                            val y = yPoints[i]
                            context.fillRect(x, y, 2.0, 2.0)
                        }
                    }
                    LineType.SEGMENTS -> {
                        var i = 0
                        while (i < n - 1) {
                            val x1 = xPoints[i]
                            val y1 = yPoints[i]
                            val x2 =  xPoints[i + 1]
                            val y2 = yPoints[i + 1]
                            context.strokeLine(x1, y1, x2, y2)
                            i += 2
                        }
                    }
                    LineType.RECT_UNFIL_DOTES -> {
                        for (i in 0 until n) {
                            val x = xPoints[i]
                            val y = yPoints[i]
                            context.strokeRect(x, y, 1.0, 1.0)
                        }
                    }
                    LineType.CIRCLE_FILL_DOTES -> {
                        for (i in 0 until n) {
                            val x = xPoints[i]
                            val y = yPoints[i]
                            context.fillOval(x, y, 2.0, 2.0)
                        }
                    }
                    LineType.CIRCLE_UNFILL_DOTES -> {
                        for (i in 0 until n) {
                            val x = xPoints[i]
                            val y = yPoints[i]
                            context.strokeOval(x, y, 1.0, 1.0)
                        }
                    }
                }
            }
        }
        context.lineWidth = previousLineSize
    }
}