package ru.nstu.grin.common.model

import java.io.Serializable

data class Point(
    val x: Double,
    val y: Double,
    var xGraphic: Double? = null,
    var yGraphic: Double? = null
) : Serializable, Cloneable {
    public override fun clone(): Any {
        return Point(
            x = x,
            y = y,
            xGraphic = xGraphic,
            yGraphic = yGraphic
        )
    }

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }


    private companion object {
        const val DELTA = 5
    }
}