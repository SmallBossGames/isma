package ru.nstu.grin.concatenation.function.transform

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
import ru.nstu.grin.common.model.WaveletDirection
import ru.nstu.grin.common.model.WaveletTransformFun

class WaveletTransformer(
    val waveletTransformFun: WaveletTransformFun,
    val waveletDirection: WaveletDirection,
): IAsyncPointsTransformer {
    private val wavelet: Wavelet = when (waveletTransformFun) {
        WaveletTransformFun.HAAR1 -> Haar1()
        WaveletTransformFun.COIFLET5 -> Coiflet5()
        WaveletTransformFun.BIORTHOGONAL68 -> BiOrthogonal68()
        WaveletTransformFun.DISCRETE_MAYER -> DiscreteMayer()
        WaveletTransformFun.LEGENDRE3 -> Legendre3()
        else -> {
            throw IllegalArgumentException("Something went wrong")
        }
    }

    override suspend fun transform(x: DoubleArray, y: DoubleArray) = makeWaveletTransform(x, y)

    private fun makeWaveletTransform(
        xArray: DoubleArray,
        yArray: DoubleArray,
    ): Pair<DoubleArray, DoubleArray> {
        val compressor = CompressorPeaksAverage()

        val transform = Transform(
            AncientEgyptianDecomposition(
                FastWaveletTransform(wavelet)
            )
        )

        return when (waveletDirection) {
            WaveletDirection.X -> {
                val xTransformed = transform.reverse(compressor.compress(transform.forward(xArray))).apply { sort() }

                Pair(xTransformed, yArray)
            }
            WaveletDirection.Y -> {
                val yTransformed = transform.reverse(compressor.compress(transform.forward(yArray))).apply { sort() }

                Pair(xArray, yTransformed)
            }
            WaveletDirection.BOTH -> {
                val xTransformed = transform.reverse(compressor.compress(transform.forward(xArray))).apply { sort() }
                val yTransformed = transform.reverse(compressor.compress(transform.forward(yArray))).apply { sort() }

                Pair(xTransformed, yTransformed)
            }
        }
    }



}