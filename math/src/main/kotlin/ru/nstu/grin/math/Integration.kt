package ru.nstu.grin.math

import ru.nstu.grin.model.MathPoint

class Integration {
    fun trapeze(points: List<MathPoint>): Double {
        var sum = 0.0
        for (i in 0 until points.size - 1) {
            sum += (points[i].y + points[i + 1].y) / 2 * (points[i + 1].x - points[i].x)
        }
        return sum
    }
}