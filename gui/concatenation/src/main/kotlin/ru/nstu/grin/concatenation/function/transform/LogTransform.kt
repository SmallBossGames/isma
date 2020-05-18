package ru.nstu.grin.concatenation.function.transform

import ru.nstu.grin.common.model.Point
import kotlin.math.log10

class LogTransform(
    private val isXLogarithm: Boolean,
    private val xLogBase: Double,
    private val isYLogarithm: Boolean,
    private val yLogBase: Double
) : Transform {
    override fun transform(point: Point): Point? {
        val x = logarithmTransform(point.x, isXLogarithm) ?: return null
        val y = logarithmTransform(point.y, isYLogarithm) ?: return null
        return Point(x, y)
    }

    private fun logarithmTransform(number: Double, condition: Boolean): Double? = when {
        condition -> {
            if (number < 0) {
                null
            } else {
                log10(number)
            }
        }
        else -> number
    }
}