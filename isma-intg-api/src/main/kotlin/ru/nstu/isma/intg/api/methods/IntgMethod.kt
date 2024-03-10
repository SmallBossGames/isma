package ru.nstu.isma.intg.api.methods

interface IntgMethod {
    val stageCalculators: Array<StageCalculator>?

    val accuracyController: AccuracyIntgController?

    val stabilityController: StabilityIntgController?

    fun nextY(step: Double, k: DoubleArray, y: Double, f: Double): Double
}

class IntegrationMethodRungeKutta(
    val stageCalculators: Array<StageCalculator> = emptyArray(),
    val accuracyController: AccuracyIntgController? = null,
    val stabilityController: StabilityIntgController? = null,
    val nextY: (step: Double, k: DoubleArray, y: Double, f: Double) -> Double
)