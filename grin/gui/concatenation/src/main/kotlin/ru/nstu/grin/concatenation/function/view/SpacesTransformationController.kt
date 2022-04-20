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
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformer
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.function.model.*
import ru.nstu.grin.math.Derivatives
import ru.nstu.grin.model.MathPoint

class SpacesTransformationController(
    private val matrixTransformer: MatrixTransformer,
    private val model: ConcatenationCanvasModel,
) {
    suspend fun transformSpaces() = coroutineScope {
        model.cartesianSpaces.forEach { space ->
            space.functions.forEach { function ->
                ensureActive()

                if (function.isHide) {
                    function.pixelsToDraw = null
                } else{
                    launch {
                        function.pixelsToDraw = transformPoints(
                            function,
                            space.xAxis,
                            space.yAxis,
                        )
                    }
                }
            }
        }
    }

    private suspend fun transformPoints(
        function: ConcatenationFunction,
        xAxis: ConcatenationAxis,
        yAxis: ConcatenationAxis,
    ): Pair<DoubleArray, DoubleArray> = coroutineScope {
        val xScaleProperties = xAxis.scaleProperties
        val yScaleProperties = yAxis.scaleProperties

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

        val transformedPoints = function.transformedPoints

        val xResults = DoubleArray(transformedPoints.first.size)
        val yResults = DoubleArray(transformedPoints.second.size)

        for (i in transformedPoints.first.indices) {
            ensureActive()

            xResults[i] = matrixTransformer.transformUnitsToPixel(
                transformedPoints.first[i],
                xScaleProperties,
                xAxis.direction,
            )
            yResults[i] = matrixTransformer.transformUnitsToPixel(
                transformedPoints.second[i],
                yScaleProperties,
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