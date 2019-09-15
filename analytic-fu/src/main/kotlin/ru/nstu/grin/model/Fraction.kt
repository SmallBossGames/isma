package ru.nstu.grin.model

data class Fraction(
    val numerator: Expression,
    val delimiter: Expression
) : Calculated {

    override fun calculate(x: Double): Double {
        return numerator.calculate(x) / delimiter.calculate(x)
    }
}