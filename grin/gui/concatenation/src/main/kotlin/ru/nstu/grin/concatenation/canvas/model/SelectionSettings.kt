package ru.nstu.grin.concatenation.canvas.model

import ru.nstu.grin.common.model.Point

data class SelectionSettings(
    var isFirstPointSelected: Boolean = false,
    var isSecondPointSelected: Boolean = false,
    var firstPoint: Point = Point.Zero,
    var secondPoint: Point = Point.Zero
) {
    fun dropToDefault() {
        isFirstPointSelected = false
        isSecondPointSelected = false
        firstPoint = Point.Zero
        secondPoint = Point.Zero
    }

    fun getArea(): Double {
        if (!isFirstPointSelected || !isSecondPointSelected) {
            return 0.0
        }

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