package ru.nstu.grin.math

object Derivatives {
    fun leftDerivative(x: DoubleArray, y: DoubleArray, degree: Int): Pair<DoubleArray, DoubleArray> {
        if (degree == 0){
            return Pair(x, y)
        }

        for(i in 1..degree){
            for(j in 0 until (y.size - i)){
                y[j] = (y[j + 1] - y[j]) / (x[j + 1] - x[j])
            }
        }

        return Pair(x.copyOfRange(0, x.size - degree), y.copyOfRange(0, y.size - degree))
    }

    fun rightDerivative(x: DoubleArray, y: DoubleArray, degree: Int): Pair<DoubleArray, DoubleArray> {
        if (degree == 0){
            return Pair(x, y)
        }

        for(i in 1..degree){
            for(j in (y.size - 1) downTo i) {
                y[j] = (y[j] - y[j - 1]) / (x[j] - x[j - 1])
            }
        }

        return Pair(x.copyOfRange(degree, x.size), y.copyOfRange(degree, y.size))
    }

    fun bothDerivatives(x: DoubleArray, y: DoubleArray, degree: Int): Pair<DoubleArray, DoubleArray> {
        if (degree == 0){
            return Pair(x, y)
        }

        val tempY = DoubleArray(y.size)

        for(i in 1..degree){
            for(j in i until (y.size - i)){
                tempY[j] = (y[j + 1] - y[j - 1]) / (x[j + 1] - x[j - 1])
            }

            tempY.copyInto(y)
        }

        return Pair(x.copyOfRange(degree, x.size - degree), y.copyOfRange(degree, y.size - degree))
    }
}