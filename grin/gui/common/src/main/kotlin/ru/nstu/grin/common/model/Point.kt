package ru.nstu.grin.common.model

import java.io.Serializable
import kotlin.math.absoluteValue

data class Point(
    var x: Double,
    var y: Double,
) : Serializable {

    companion object {
        private const val DELTA = 5.0
        val Zero = Point(0.0, 0.0)

        fun isNearBy(x1:Double, y1: Double, x2: Double, y2: Double) =
            (x1 - x2).absoluteValue < DELTA && (y1 - y2).absoluteValue < DELTA

        fun estimateDistance(x1: Double, y1: Double, x2: Double, y2: Double): Double {
            val xOffset = x2 - x1
            val yOffset = y2 - y1

            return xOffset*xOffset + yOffset*yOffset
        }
    }
}