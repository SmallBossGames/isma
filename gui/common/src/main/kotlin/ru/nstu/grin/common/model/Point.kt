package ru.nstu.grin.common.model

import java.io.Serializable

data class Point(
    val x: Double,
    val y: Double,
    var xGraphic: Double? = null,
    var yGraphic: Double? = null
) : Serializable {
    fun isNearBy(eventX: Double, eventY: Double): Boolean {
        val x = xGraphic
        val y = yGraphic
        if (x == null || y == null) return false

        return when {
            (eventX < x + DELTA && eventX > x - DELTA) &&
                (eventY < y + DELTA && eventY > y - DELTA) -> {
                true
            }
            else -> {
                false
            }
        }
    }

    private companion object {
        const val DELTA = 5
    }
}