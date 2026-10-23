package ru.nstu.isma.intg.api.methods

class IntegrationMethodRungeKutta(
    override val stageCalculators: Array<StageCalculator> = emptyArray(),
    override val accuracyController: AccuracyIntgController? = null,
    override val stabilityController: StabilityIntgController? = null,
    val nextY: (step: Double, k: DoubleArray, y: Double, f: Double) -> Double,
) : IIntegrationMethod {
    override fun nextY(step: Double, stages: DoubleArray, y: Double, f: Double): Double {
        val fn: (Double, DoubleArray, Double, Double) -> Double = this.nextY
        return fn(step, stages, y, f)
    }
}
