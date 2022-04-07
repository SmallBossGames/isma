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
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.common.model.WaveletDirection
import ru.nstu.grin.common.model.WaveletTransformFun
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformer
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.model.project.ConcatenationAxisSnapshot
import ru.nstu.grin.concatenation.canvas.model.project.toModel
import ru.nstu.grin.concatenation.canvas.model.project.toSnapshot
import ru.nstu.grin.concatenation.function.model.DerivativeDetails
import ru.nstu.grin.concatenation.function.model.DerivativeType
import ru.nstu.grin.concatenation.function.model.MirrorDetails
import ru.nstu.grin.concatenation.function.model.WaveletDetails
import ru.nstu.grin.math.Derivatives
import ru.nstu.grin.model.MathPoint
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class SpacesTransformationController(
    private val matrixTransformer: MatrixTransformer,
    private val model: ConcatenationCanvasModel,
) {
    private val derivativesCache = ConcurrentHashMap<DerivativeCacheKey, List<Point>>()
    private val waveletCache = ConcurrentHashMap<WaveletCacheKey, List<Point>>()

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
        model.cartesianSpaces.forEach { space ->
            space.functions.forEach { function ->
                ensureActive()

                if (function.isHide) {
                    function.pixelsToDraw = null
                } else{
                    launch {
                        function.pixelsToDraw = transformPoints(
                            function.id,
                            function.points,
                            space.xAxis.toSnapshot(),
                            space.yAxis.toSnapshot(),
                            function.mirrorDetails,
                            function.derivativeDetails,
                            function.waveletDetails,
                        )
                    }
                }
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
    ): Pair<DoubleArray, DoubleArray> = coroutineScope {

        /*val transforms = listOf(
            LogTransform(
                xAxis.axisMarkType == AxisMarkType.LOGARITHMIC,
                xAxis.settings.logarithmBase,
                yAxis.axisMarkType == AxisMarkType.LOGARITHMIC,
                yAxis.settings.logarithmBase
            ),
            MirrorTransform(mirrorDetails.isMirrorX, mirrorDetails.isMirrorY)
        )*/

        /*val waveletPoints = waveletDetails?.let {
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
        } ?: points*/

        /*val transformedPoints = derivativeDetails?.let {
            val key = DerivativeCacheKey(
                functionId,
                it.type,
                it.degree
            )
            val cached = derivativesCache[key]
            if (cached == null) {
                val new = makeDerivative(waveletPoints, it)
                derivativesCache[key] = new
                new
            } else {
                cached
            }
        } ?: waveletPoints*/

        val transformedPoints = points

        val xResults = DoubleArray(transformedPoints.size)
        val yResults = DoubleArray(transformedPoints.size)

        for (i in transformedPoints.indices) {
            ensureActive()

            xResults[i] = matrixTransformer.transformUnitsToPixel(
                transformedPoints[i].x,
                xAxis.settings.toModel(),
                xAxis.direction,
            )
            yResults[i] = matrixTransformer.transformUnitsToPixel(
                transformedPoints[i].y,
                yAxis.settings.toModel(),
                yAxis.direction,
            )
        }

        Pair(xResults, yResults)
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