package ru.nstu.grin.concatenation.function.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformerController
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import ru.nstu.grin.concatenation.function.model.DerivativeDetails
import ru.nstu.grin.concatenation.function.model.DerivativeType
import ru.nstu.grin.concatenation.function.model.LineType
import ru.nstu.grin.concatenation.function.model.MirrorDetails
import ru.nstu.grin.concatenation.function.transform.LogTransform
import ru.nstu.grin.concatenation.function.transform.MirrorTransform
import ru.nstu.grin.math.Derivatives
import ru.nstu.grin.model.MathPoint
import tornadofx.Controller
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class ConcatenationFunctionDrawElement : ChainDrawElement, Controller() {
    private val matrixTransformer: MatrixTransformerController by inject()
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val derivativesCache2: MutableMap<DerivativeCacheKey, List<Point>> = ConcurrentHashMap()

    data class DerivativeCacheKey(
        val functionId: UUID,
        val type: DerivativeType,
        val degree: Int
    )

    override fun draw(context: GraphicsContext) {
        val previousLineSize = context.lineWidth
        for (cartesianSpace in model.cartesianSpaces) {
            for (function in cartesianSpace.functions) {
                if (function.isHide) continue
                context.stroke = function.functionColor
                context.fill = function.functionColor
                context.lineWidth = function.lineSize
                transformPoints(
                    function.id,
                    function.points,
                    cartesianSpace.xAxis,
                    cartesianSpace.yAxis,
                    function.getMirrorDetails(),
                    function.getDerivativeDetails()
                )

                val points = function.points

                val xPoints = points.mapNotNull { it.xGraphic }.toDoubleArray()
                val yPoints = points.mapNotNull { it.yGraphic }.toDoubleArray()
                val n = xPoints.size

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

    private fun transformPoints(
        functionId: UUID,
        points: List<Point>,
        xAxis: ConcatenationAxis,
        yAxis: ConcatenationAxis,
        mirrorDetails: MirrorDetails,
        derivativeDetails: DerivativeDetails?
    ) {
        val transforms = listOf(
            LogTransform(xAxis.settings.isLogarithmic, yAxis.settings.isLogarithmic),
            MirrorTransform(mirrorDetails.isMirrorX, mirrorDetails.isMirrorY)
        )
        val currentTime = System.currentTimeMillis()
        val transformedPoints = derivativeDetails?.let {
            val key = DerivativeCacheKey(functionId, it.type, it.degree)
            val cached = derivativesCache2[key]
            if (cached == null) {
                val new = makeDerivative(points, it)
                derivativesCache2[key] = new
                new
            } else {
                cached
            }
        } ?: points
        println("Passed time ${System.currentTimeMillis() - currentTime}")
        for (i in transformedPoints.indices) {
            var temp: Point? = transformedPoints[i]
            for (transform in transforms) {
                temp = temp?.let { transform.transform(it) }
            }
            if (temp != null) {
                points[i].xGraphic = matrixTransformer.transformUnitsToPixel(
                    temp.x,
                    xAxis.settings, xAxis.direction
                )
                points[i].yGraphic = matrixTransformer.transformUnitsToPixel(
                    temp.y,
                    yAxis.settings,
                    yAxis.direction
                )
            } else {
                points[i].xGraphic = null
                points[i].yGraphic = null
            }
        }
    }

    private fun makeDerivative(points: List<Point>, details: DerivativeDetails): List<Point> {
        val derivative = Derivatives()
        val mathPoints = points.map { MathPoint(it.x, it.y) }
        return when (details.type) {
            DerivativeType.LEFT -> derivative.leftDerivative(mathPoints, details.degree)
            DerivativeType.RIGHT -> derivative.rightDerivative(mathPoints, details.degree)
            DerivativeType.BOTH -> derivative.bothDerivatives(mathPoints, details.degree)
        }.map { Point(it.x, it.y) }
    }
}