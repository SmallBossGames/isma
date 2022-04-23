package ru.nstu.grin.math

import ru.nstu.grin.model.MathPoint

object Integration {
    fun trapeze(points: List<MathPoint>): Double {
        var sum = 0.0
        for (i in 0 until points.size - 1) {
            sum += (points[i].y + points[i + 1].y) / 2 * (points[i + 1].x - points[i].x)
        }
        return sum
    }

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