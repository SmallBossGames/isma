package ru.nstu.grin.concatenation.function.transform

import kotlin.math.log
import kotlin.math.log10
import kotlin.math.log2

data class LogTransformer(
    val isXLogarithm: Boolean,
    val xLogBase: Double,
    val isYLogarithm: Boolean,
    val yLogBase: Double
): IAsyncPointsTransformer {
    override suspend fun transform(x: DoubleArray, y: DoubleArray): Pair<DoubleArray, DoubleArray> {
        if(isXLogarithm){
            logArray(x, xLogBase)
        }

        if(isYLogarithm){
            logArray(y, yLogBase)
        }

        return Pair(x, y)
    }

    private fun logArray(values: DoubleArray, logBase: Double){
        when (logBase){
            2.0 -> for (i in values.indices) values[i] = log2(values[i])
            10.0 -> for (i in values.indices) values[i] = log10(values[i])
            else -> for (i in values.indices) values[i] = log(values[i], logBase)
        }
    }
}