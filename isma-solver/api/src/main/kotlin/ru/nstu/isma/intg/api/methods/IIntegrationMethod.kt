package ru.nstu.isma.intg.api.methods

interface IIntegrationMethod {
    val stageCalculators: Array<StageCalculator>
    val accuracyController: AccuracyIntgController?
    val stabilityController: StabilityIntgController?
    fun nextY(step: Double, stages: DoubleArray, y: Double, f: Double): Double
}
