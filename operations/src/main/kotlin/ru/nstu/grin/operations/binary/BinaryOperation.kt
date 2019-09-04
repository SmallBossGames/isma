package ru.nstu.grin.operations.binary

interface BinaryOperation {
    fun execute(x1: Double, x2: Double): Pair<Double, Double>
}