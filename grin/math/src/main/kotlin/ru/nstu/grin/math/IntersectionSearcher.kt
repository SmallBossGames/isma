package ru.nstu.grin.math

import ru.nstu.grin.model.Function

class IntersectionSearcher {
    fun findIntersections(first: Function, second: Function): List<Pair<Double, Double>> {
        val firstSet = mutableSetOf<Pair<Double, Double>>()
        val secondSet = mutableSetOf<Pair<Double, Double>>()

        for (i in first.xPoints.indices){
            firstSet.add(Pair(first.xPoints[i], first.yPoints[i]))
        }

        for (i in second.xPoints.indices){
            secondSet.add(Pair(second.xPoints[i], second.yPoints[i]))
        }

        return firstSet.intersect(secondSet).toList()
    }
}