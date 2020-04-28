package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.common.model.DrawSize

class DeltaSizeCalculator {
    fun calculateDelta(drawSize: DrawSize): Double {
        return (drawSize.maxX - drawSize.minX) / DEFAULT_SCALE
    }

    private companion object {
        const val DEFAULT_SCALE = 100
    }
}