package ru.nstu.grin.math

import ru.nstu.grin.model.Function

class IntersectionSearcher {
    fun findIntersections(first: Function, second: Function): List<Pair<Double, Double>> {
        return first.points.intersect(second.points).toList()
    }
}