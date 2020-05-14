package ru.nstu.grin.math

import ru.nstu.grin.model.MathPoint

class Derivatives {
    fun leftDeriviative(current: List<MathPoint>, degree: Int): List<MathPoint> {
        val result = mutableListOf<MathPoint>()
        if (degree == 0) return current

        for (i in 0 until current.size - 1) {
            result.add(
                MathPoint(
                    current[i].x,
                    (current[i + 1].y - current[i].y) / (current[i + 1].x - current[i].x)
                )
            )
        }

        return leftDeriviative(current, degree)
    }

    fun rightDerivative(current: List<MathPoint>, degree: Int): List<MathPoint> {
        val result = mutableListOf<MathPoint>()
        if (degree == 0) return current

        for (i in 0 until current.size) {
            result.add(
                MathPoint(
                    current[i].x,
                    (current[i].y - current[i - 1].y) / (current[i].x - current[i - 1].x)
                )
            )
        }

        return rightDerivative(current, degree)
    }

    fun bothDerivatives(current: List<MathPoint>, degree: Int): List<MathPoint> {
        val result = mutableListOf<MathPoint>()

        if (degree == 0) return current

        for (i in 0 until current.size - 1) {
            result.add(
                MathPoint(
                    current[i].x,
                    (current[i + 1].y - current[i - 1].y) / (current[i + 1].x - current[i - 1].x)
                )
            )
        }
        return leftDeriviative(result, degree - 1)
    }

//    public double RightDerivativeFirst(double x)
//    {
//        return (_function(x) - _function(x - _step)) / (_step);
//    }
//
//    public double DerivativesSecond(double x)
//    {
//        return (_function(x + 2 * _step) - 2 * _function(x) + _function(x - 2 * _step)) / (4 * _step * _step);
//    }
}