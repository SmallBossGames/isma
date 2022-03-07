package ru.nstu.grin.concatenation.canvas.model

import ru.nstu.grin.common.model.Point
import kotlin.math.max
import kotlin.math.min

data class SelectionSettings(
    var isFirstPointSelected: Boolean = false,
    var isSecondPointSelected: Boolean = false,
    var firstPoint: Point = Point.Zero,
    var secondPoint: Point = Point.Zero
) {
    fun reset() {
        isFirstPointSelected = false
        isSecondPointSelected = false
        firstPoint = Point.Zero
        secondPoint = Point.Zero
    }

    val area get() =
        if (isFirstPointSelected && isSecondPointSelected) {
            (maxY - minY) * (maxX - minX)
        }
        else {
            0.0
        }


    val minX get() = min(firstPoint.x, secondPoint.x)

    val maxX get() = max(firstPoint.x, secondPoint.x)

    val minY get() = min(firstPoint.y, secondPoint.y)

    val maxY get() = max(firstPoint.y, secondPoint.y)
}