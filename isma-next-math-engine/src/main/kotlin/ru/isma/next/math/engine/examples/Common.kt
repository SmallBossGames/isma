package ru.isma.next.math.engine.examples

import kotlin.math.abs

fun findDelta(result: DoubleArray, reference: DoubleArray): Double {
    var max = 0.0
    for (i in result.indices){
        val delta = abs(result[i] - reference[i])
        max = kotlin.math.max(max, delta)
    }
    return max
}

fun printResultWithDelta(result: DoubleArray, reference: DoubleArray){
    val delta = findDelta(result, reference)
    println("Delta: $delta, results: ")
    for (i in result)
        println(i)
    println()
}