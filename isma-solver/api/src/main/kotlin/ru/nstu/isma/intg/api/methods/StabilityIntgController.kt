package ru.nstu.isma.intg.api.methods

abstract class StabilityIntgController: IntgController() {
    abstract fun predictNextStepSize(toPoint: IntgPoint): Double

    protected abstract fun getStabilityInterval(): Double

    protected abstract fun getMaxJacobiEigenValue(point: IntgPoint): Double
}