package ru.nstu.isma.next.core.common

/**
 * Created by Алексей on 03.10.14.
 */
class Point(private var x: Double, private var y: Double) {
    fun getX(): Double {
        return x
    }

    fun setX(x: Double) {
        this.x = x
    }

    fun getY(): Double {
        return y
    }

    fun setY(y: Double) {
        this.y = y
    }
}