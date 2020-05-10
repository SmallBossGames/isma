package ru.nstu.grin.concatenation.canvas.model

import ru.nstu.grin.common.model.Point

data class SelectionSettings(
    var isSelected: Boolean = false,
    var firstPoint: Point = Point(0.0, 0.0),
    var secondPoint: Point = Point(0.0, 0.0)
) {
    fun dropToDefault() {
        isSelected = false
        firstPoint = Point(0.0, 0.0)
        secondPoint = Point(0.0, 0.0)
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