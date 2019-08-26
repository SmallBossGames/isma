package ru.nstu.grin

import java.util.function.Function

object FunctionUtils {
    fun drawByFunction(min: Double, max: Double, step: Double, func: Function<Double, Double>): List<Double> {
        var current = min;
        val results = mutableListOf<Double>()
        while (current < max) {
            results.add(func.apply(current))
            current += step;
        }
        return results
    }

    private fun concat(list: List<Double>): String {
        val sb = StringBuilder()
        for (i in list) {
            sb.append(i).append(" ")
        }
        return sb.toString()
    }
}

fun main() {
    FunctionUtils.drawByFunction(0.0, 100.0, 0.1, Function {
        myFunc(it)
    })
}

fun myFunc(x: Double): Double {
    return x * x
}