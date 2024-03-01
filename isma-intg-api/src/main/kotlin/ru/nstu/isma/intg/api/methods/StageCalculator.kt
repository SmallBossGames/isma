package ru.nstu.isma.intg.api.methods

abstract class StageCalculator {
    abstract fun yk(step: Double, y: Double, f: Double, stages: DoubleArray): Double

    abstract fun k(step: Double, y: Double, f: Double, stages: DoubleArray, stagesY: Double, stagesF: Double): Double
}