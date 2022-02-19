package ru.nstu.grin.common.model

import java.io.Serializable

data class Point(
    var x: Double,
    var y: Double,
    var xGraphic: Double? = null,
    var yGraphic: Double? = null
) : Serializable {

    fun isNearBy(eventX: Double, eventY: Double): Boolean {
        val x = xGraphic
        val y = yGraphic
        if (x == null || y == null) return false

        return (eventX < x + DELTA && eventX > x - DELTA) && (eventY < y + DELTA && eventY > y - DELTA)
    }


    companion object {
        private const val DELTA = 5
        val Zero = Point(0.0, 0.0)
    }
}