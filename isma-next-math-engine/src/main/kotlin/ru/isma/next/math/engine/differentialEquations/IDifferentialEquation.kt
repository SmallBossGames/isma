package ru.isma.next.math.engine.differentialEquations

interface IDifferentialEquation {
    fun getDifferential(t: Double, inY: DoubleArray, outY: DoubleArray)
}