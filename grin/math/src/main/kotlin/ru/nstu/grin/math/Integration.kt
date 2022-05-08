package ru.nstu.grin.math

object Integration {

    fun trapeze(x: DoubleArray, y: DoubleArray, initialValue: Double): Pair<DoubleArray, DoubleArray> {
        if (x.isEmpty()){
            return Pair(doubleArrayOf(), doubleArrayOf())
        }

        val resultY = DoubleArray(x.size)

        resultY[0] = initialValue

        for (i in 1 until resultY.size){
            resultY[i] = (y[i] + y[i - 1]) / 2 * (x[i] - x[i - 1]) + resultY[i - 1]
        }

        return Pair(x, resultY)
    }
}