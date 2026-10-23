package ru.nstu.isma.intg.lib.rungeKutta.rungeKuttaFehlberg.internal

import ru.nstu.isma.intg.api.methods.StageCalculator

class RkFehlbergStage2Calculator : StageCalculator() {
    override fun yk(step: Double, y: Double, f: Double, stages: DoubleArray): Double {
        return y + 0.25 * stages[0]
    }

    override fun k(
        step: Double, y: Double, f: Double, stages: DoubleArray, stagesY: Double,
        stagesF: Double
    ): Double {
        return step * stagesF
    }
}
