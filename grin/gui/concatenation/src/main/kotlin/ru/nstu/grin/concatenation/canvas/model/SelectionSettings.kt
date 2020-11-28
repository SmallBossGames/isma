package ru.nstu.grin.concatenation.canvas.model

import ru.nstu.grin.common.model.Point

data class SelectionSettings(
    var isSelected: Boolean = false,
    var firstPoint: Point = Point(-1.0, -1.0),
    var secondPoint: Point = Point(-1.0, -1.0)
) {
    fun dropToDefault() {
        isSelected = false
        firstPoint = Point(-1.0, -1.0)
        secondPoint = Point(-1.0, -1.0)
    }

    fun getArea(): Double {
        if (secondPoint.x == -1.0 || secondPoint.y == -1.0) return 0.0
        return (getMaxY() - getMinY()) * (getMaxX() - getMinX())
    }

    fun getMinX(): Double {
        return if (firstPoint.x < secondPoint.x) {
            firstPoint.x
        } else {
            secondPoint.x
        }
    }

    fun getMaxX(): Double {
        return if (firstPoint.x > secondPoint.x) {
            firstPoint.x
        } else {
            secondPoint.x
        }
    }

    fun getMinY(): Double {
        return if (firstPoint.y < secondPoint.y) {
            firstPoint.y
        } else {
            secondPoint.y
        }
    }

    fun getMaxY(): Double {
        return if (firstPoint.y > secondPoint.y) {
            firstPoint.y
        } else {
            secondPoint.y
        }
    }
}