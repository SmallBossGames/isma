package ru.nstu.isma.intg.api.methods

import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver
import java.io.Serializable

abstract class AccuracyIntgController: IntgController() {
    var accuracy = 0.0

    abstract fun tune(
        fromPoint: IntgPoint, stages: Array<DoubleArray>, stepSolver: DaeSystemStepSolver
    ): AccuracyResults

    protected abstract fun getLocalErrorEstimation(deStages: DoubleArray): Double

    protected abstract fun getActualAccuracy(step: Double, localError: Double): Double

    protected abstract fun getQ(accuracy: Double, actualAccuracy: Double): Double


    class AccuracyResults(val tunedStep: Double, val tunedStages: Array<DoubleArray>) : Serializable
}