package ru.nstu.grin.operators

class BinaryOperatorImpl(val sign: Char) : BinaryOperator {
    override fun calculate(x: Double, y: Double): Double {
        var result = 0.0
        when (sign) {
            '*' -> {
                return x * y
            }
            '\\' -> {
                return x//y
            }
            '+' -> {

            }
            '-' -> {

            }
        }
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}