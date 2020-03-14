package ru.nstu.grin.common.model

import java.io.Serializable

data class Point(val x: Double, val y: Double) : Serializable {
    fun isNearBy(x: Double, y: Double): Boolean {
        return when {
            (x < this.x + DELTA && x > this.x - DELTA) &&
                (y < this.y + DELTA && y > this.y - DELTA) -> {
                true
            }
            else -> {
                false
            }
        }
    }

    private companion object {
        const val DELTA = 0.5
    }
}