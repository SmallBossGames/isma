package ru.nstu.grin.math

import ru.nstu.grin.model.MathPoint

class Derivatives {
    fun leftDerivative(current: List<MathPoint>, degree: Int): List<MathPoint> {
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

        return leftDerivative(result, degree - 1)
    }

    fun rightDerivative(current: List<MathPoint>, degree: Int): List<MathPoint> {
        val result = mutableListOf<MathPoint>()
        if (degree == 0) return current

        for (i in 1 until current.size) {
            result.add(
                MathPoint(
                    current[i].x,
                    (current[i].y - current[i - 1].y) / (current[i].x - current[i - 1].x)
                )
            )
        }

        return leftDerivative(result, degree - 1)
    }

    fun bothDerivatives(current: List<MathPoint>, degree: Int): List<MathPoint> {
        val result = mutableListOf<MathPoint>()

        if (degree == 0) return current

        for (i in 1 until current.size - 1) {
            result.add(
                MathPoint(
                    current[i].x,
                    (current[i + 1].y - current[i - 1].y) / (current[i + 1].x - current[i - 1].x)
                )
            )
        }
        return leftDerivative(result, degree - 1)
    }
}