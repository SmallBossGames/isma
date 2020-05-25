package ru.nstu.grin.concatenation.function.view

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import jwave.Transform
import jwave.transforms.AncientEgyptianDecomposition
import jwave.transforms.FastWaveletTransform
import jwave.transforms.wavelets.Wavelet
import jwave.transforms.wavelets.biorthogonal.BiOrthogonal68
import jwave.transforms.wavelets.coiflet.Coiflet5
import jwave.transforms.wavelets.haar.Haar1
import jwave.transforms.wavelets.legendre.Legendre3
import jwave.transforms.wavelets.other.DiscreteMayer
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.common.model.WaveletDirection
import ru.nstu.grin.common.model.WaveletTransformFun
import ru.nstu.grin.common.view.ChainDrawElement
import ru.nstu.grin.concatenation.axis.model.AxisMarkType
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformerController
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.function.model.*
import ru.nstu.grin.concatenation.function.transform.LogTransform
import ru.nstu.grin.concatenation.function.transform.MirrorTransform
import ru.nstu.grin.math.Derivatives
import ru.nstu.grin.model.MathPoint
import tornadofx.Controller
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class ConcatenationFunctionDrawElement : ChainDrawElement, Controller() {
    private val matrixTransformer: MatrixTransformerController by inject()
    private val model: ConcatenationCanvasModel by inject()
    private val derivativesCache2: MutableMap<DerivativeCacheKey, List<Point>> = ConcurrentHashMap()
    private val waveletCache: MutableMap<WaveletCacheKey, List<Point>> = ConcurrentHashMap()

    data class DerivativeCacheKey(
        val functionId: UUID,
        val type: DerivativeType,
        val degree: Int
    )

    data class WaveletCacheKey(
        val functionId: UUID,
        val waveletTransformFun: WaveletTransformFun,
        val waveletDirection: WaveletDirection
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
                    function.getDerivativeDetails(),
                    function.getWaveletDetails()
                )

                val points = function.points

                val xPoints = points.mapNotNull { it.xGraphic }.toDoubleArray()
                val yPoints = points.mapNotNull { it.yGraphic }.toDoubleArray()
                val n = if (function.getDerivativeDetails() != null) {
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

    private fun transformPoints(
        functionId: UUID,
        points: List<Point>,
        xAxis: ConcatenationAxis,
        yAxis: ConcatenationAxis,
        mirrorDetails: MirrorDetails,
        derivativeDetails: DerivativeDetails?,
        waveletDetails: WaveletDetails?
    ) {
        val transforms = listOf(
            LogTransform(
                xAxis.axisMarkType == AxisMarkType.LOGARITHMIC,
                xAxis.settings.logarithmBase,
                yAxis.axisMarkType == AxisMarkType.LOGARITHMIC,
                yAxis.settings.logarithmBase
            ),
            MirrorTransform(mirrorDetails.isMirrorX, mirrorDetails.isMirrorY)
        )
        val timeBeforeWavelet = System.currentTimeMillis()
        val waveletPoints = waveletDetails?.let {
            val key = WaveletCacheKey(functionId, it.waveletTransformFun, it.waveletDirection)
            val cached = waveletCache[key]
            if (cached == null) {
                val new = makeWaveletTransform(points, it)
                waveletCache[key] = new
                new
            } else {
                cached
            }
        } ?: points
        println("Passed time after wavelet ${System.currentTimeMillis() - timeBeforeWavelet}")

        val timeBeforeDerivative = System.currentTimeMillis()
        val transformedPoints = derivativeDetails?.let {
            val key = DerivativeCacheKey(functionId, it.type, it.degree)
            val cached = derivativesCache2[key]
            if (cached == null) {
                val new = makeDerivative(waveletPoints, it)
                derivativesCache2[key] = new
                new
            } else {
                cached
            }
        } ?: waveletPoints
        println("Passed time after derivative  ${System.currentTimeMillis() - timeBeforeDerivative}")
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

    private fun makeWaveletTransform(
        points: List<Point>,
        waveletDetails: WaveletDetails
    ): List<Point> {
        val transform = Transform(
            AncientEgyptianDecomposition(
                FastWaveletTransform(getWavelet(waveletDetails.waveletTransformFun))
            )
        )
        val xArray = points.map { it.x }.toDoubleArray()
        val yArray = points.map { it.y }.toDoubleArray()

        return when (waveletDetails.waveletDirection) {
            WaveletDirection.X -> {
                val xTransformed = transform.forward(xArray).sorted()

                xTransformed.mapIndexed { index, d ->
                    Point(d, yArray[index])
                }
            }
            WaveletDirection.Y -> {
                val yTransformed = transform.forward(yArray).sorted()

                xArray.mapIndexed { index, d ->
                    Point(d, yTransformed[index])
                }
            }
            WaveletDirection.BOTH -> {
                val xTransformed = transform.forward(xArray).sorted()
                val yTransformed = transform.forward(yArray).sorted()

                xTransformed.mapIndexed { index, d ->
                    Point(d, yTransformed[index])
                }
            }
        }
    }

    private fun getWavelet(waveletTransformFun: WaveletTransformFun): Wavelet {
        return when (waveletTransformFun) {
            WaveletTransformFun.HAAR1 -> Haar1()
            WaveletTransformFun.COIFLET5 -> Coiflet5()
            WaveletTransformFun.BIORTHOGONAL68 -> BiOrthogonal68()
            WaveletTransformFun.DISCRETE_MAYER -> DiscreteMayer()
            WaveletTransformFun.LEGENDRE3 -> Legendre3()
            else -> {
                throw IllegalArgumentException("Something went wrong")
            }
        }
    }
}