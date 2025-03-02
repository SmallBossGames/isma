package ru.nstu.isma.intg.api.methods

class IntegrationMethodRungeKutta(
    val stageCalculators: Array<StageCalculator> = emptyArray(),
    val accuracyController: AccuracyIntgController? = null,
    val stabilityController: StabilityIntgController? = null,
    val nextY: (step: Double, k: DoubleArray, y: Double, f: Double) -> Double
)
