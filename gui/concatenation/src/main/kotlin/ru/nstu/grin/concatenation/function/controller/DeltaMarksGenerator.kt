package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.common.model.DrawSize

class DeltaMarksGenerator {
    fun getDeltaMarks(drawSize: DrawSize, delta: Double, position: Direction): List<Double> {
        return when (position) {
            Direction.LEFT, Direction.RIGHT -> {
                calculateMarks(drawSize.minY, drawSize.maxY, 5 * delta)
            }
            Direction.TOP, Direction.BOTTOM -> {
                calculateMarks(drawSize.minX, drawSize.maxX, 5 * delta)
            }
        }
    }

    private fun calculateMarks(min: Double, max: Double, delta: Double): List<Double> {
        val result = mutableListOf<Double>()
        var current = min
        while (current < max) {
            result.add(current)
            current += delta
        }
        return result
    }
}