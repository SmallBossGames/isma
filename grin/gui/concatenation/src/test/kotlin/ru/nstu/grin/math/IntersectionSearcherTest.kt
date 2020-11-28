package ru.nstu.grin.math

import org.junit.jupiter.api.Test
import ru.nstu.grin.model.Function

internal class IntersectionSearcherTest {

    @Test
    fun `should intersect`() {
        val firstFunction = Function(
            points = listOf(Pair(2.0, 0.0))
        )
        val secondFunction = Function(
            points = listOf(Pair(2.0, 0.0))
        )
        firstFunction.points.intersect(secondFunction.points)
    }
}