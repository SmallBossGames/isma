package ru.nstu.grin.concatenation.function.transform

import kotlin.math.log
import kotlin.math.log10
import kotlin.math.log2

class LogTransformer(
    private val isXLogarithm: Boolean,
    private val xLogBase: Double,
    private val isYLogarithm: Boolean,
    private val yLogBase: Double
): IAsyncPointsTransformer {
    override suspend fun transform(x: DoubleArray, y: DoubleArray) {
        if(isXLogarithm){
            logArray(x, xLogBase)
        }

        if(isYLogarithm){
            logArray(y, yLogBase)
        }
    }

    private fun logArray(values: DoubleArray, logBase: Double){
        when (logBase){
            2.0 -> for (i in values.indices) log2(values[i])
            10.0 -> for (i in values.indices) log10(values[i])
            else -> for (i in values.indices) log(values[i], logBase)
        }
    }
}