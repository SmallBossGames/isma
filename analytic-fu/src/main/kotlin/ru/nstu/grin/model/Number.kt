package ru.nstu.grin.model

data class Number(
    val value: Double
) : Calculated {
    override fun calculate(x: Double): Double {
        return x
    }
}