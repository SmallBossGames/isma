package ru.nstu.grin.concatenation.function.view

import jwave.Transform
import jwave.compressions.CompressorPeaksAverage
import jwave.transforms.AncientEgyptianDecomposition
import jwave.transforms.FastWaveletTransform
import jwave.transforms.wavelets.Wavelet
import jwave.transforms.wavelets.biorthogonal.BiOrthogonal68
import jwave.transforms.wavelets.coiflet.Coiflet5
import jwave.transforms.wavelets.haar.Haar1
import jwave.transforms.wavelets.legendre.Legendre3
import jwave.transforms.wavelets.other.DiscreteMayer
import kotlinx.coroutines.coroutineScope
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.common.model.WaveletDirection
import ru.nstu.grin.common.model.WaveletTransformFun
import ru.nstu.grin.concatenation.axis.model.AxisMarkType
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformerController
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.model.project.ConcatenationAxisSnapshot
import ru.nstu.grin.concatenation.canvas.model.project.toModel
import ru.nstu.grin.concatenation.canvas.model.project.toSnapshot
import ru.nstu.grin.concatenation.function.model.DerivativeDetails
import ru.nstu.grin.concatenation.function.model.DerivativeType
import ru.nstu.grin.concatenation.function.model.MirrorDetails
import ru.nstu.grin.concatenation.function.model.WaveletDetails
import ru.nstu.grin.concatenation.function.transform.LogTransform
import ru.nstu.grin.concatenation.function.transform.MirrorTransform
import ru.nstu.grin.math.Derivatives
import ru.nstu.grin.model.MathPoint
import tornadofx.Controller
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class SpacesTransformationController: Controller() {
    private val matrixTransformer: MatrixTransformerController by inject()
    private val model: ConcatenationCanvasModel by inject()

    private val derivativesCache: MutableMap<DerivativeCacheKey, List<Point>> = ConcurrentHashMap()
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

    suspend fun transformSpaces() = coroutineScope {
        for (cartesianSpace in model.cartesianSpaces) {
            for (function in cartesianSpace.functions) {
                if (function.isHide) continue

                transformPoints(
                    function.id,
                    function.points,
                    cartesianSpace.xAxis.toSnapshot(),
                    cartesianSpace.yAxis.toSnapshot(),
                    function.mirrorDetails,
                    function.derivativeDetails,
                    function.waveletDetails,
                )
            }
        }
    }

    private suspend fun transformPoints(
        functionId: UUID,
        points: List<Point>,
        xAxis: ConcatenationAxisSnapshot,
        yAxis: ConcatenationAxisSnapshot,
        mirrorDetails: MirrorDetails,
        derivativeDetails: DerivativeDetails?,
        waveletDetails: WaveletDetails?
    ) = coroutineScope {

        val transforms = listOf(
            LogTransform(
                xAxis.axisMarkType == AxisMarkType.LOGARITHMIC,
                xAxis.settings.logarithmBase,
                yAxis.axisMarkType == AxisMarkType.LOGARITHMIC,
                yAxis.settings.logarithmBase
            ),
            MirrorTransform(mirrorDetails.isMirrorX, mirrorDetails.isMirrorY)
        )

        val waveletPoints = waveletDetails?.let {
            val key = WaveletCacheKey(
                functionId,
                it.waveletTransformFun,
                it.waveletDirection
            )
            val cached = waveletCache[key]
            if (cached == null) {
                val new = makeWaveletTransform(points, it)
                waveletCache[key] = new
                new
            } else {
                cached
            }
        } ?: points

        val transformedPoints = derivativeDetails?.let {
            val key = DerivativeCacheKey(functionId, it.type, it.degree)
            val cached = derivativesCache[key]
            if (cached == null) {
                val new = makeDerivative(waveletPoints, it)
                derivativesCache[key] = new
                new
            } else {
                cached
            }
        } ?: waveletPoints

        for (i in transformedPoints.indices) {
            var temp: Point? = transformedPoints[i]
            for (transform in transforms) {
                temp = temp?.let { transform.transform(it) }
            }
            if (temp != null) {
                points[i].xGraphic = matrixTransformer.transformUnitsToPixel(
                    temp.x,
                    xAxis.settings.toModel(),
                    xAxis.direction,
                )
                points[i].yGraphic = matrixTransformer.transformUnitsToPixel(
                    temp.y,
                    yAxis.settings.toModel(),
                    yAxis.direction,
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
        val compressor = CompressorPeaksAverage()

        val transform = Transform(
            AncientEgyptianDecomposition(
                FastWaveletTransform(getWavelet(waveletDetails.waveletTransformFun))
            )
        )
        val xArray = points.map { it.x }.toDoubleArray()
        val yArray = points.map { it.y }.toDoubleArray()

        return when (waveletDetails.waveletDirection) {
            WaveletDirection.X -> {
                val xTransformed = transform.reverse(compressor.compress(transform.forward(xArray))).sorted()

                xTransformed.mapIndexed { index, d ->
                    Point(d, yArray[index])
                }
            }
            WaveletDirection.Y -> {
                val yTransformed = transform.reverse(compressor.compress(transform.forward(yArray))).sorted()

                xArray.mapIndexed { index, d ->
                    Point(d, yTransformed[index])
                }
            }
            WaveletDirection.BOTH -> {
                val xTransformed = transform.reverse(compressor.compress(transform.forward(xArray))).sorted()
                val yTransformed = transform.reverse(compressor.compress(transform.forward(yArray))).sorted()

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