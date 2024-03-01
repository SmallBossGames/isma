package ru.nstu.isma.intg.lib.rungeKutta.rungeKuttaFehlberg.internal

import ru.nstu.isma.intg.api.methods.StageCalculator

class RkFehlbergStage3Calculator : StageCalculator() {
    override fun yk(step: Double, y: Double, f: Double, stages: DoubleArray): Double {
        return y + 0.09375 * stages[0] + 0.28125 * stages[1]
    }

    override fun k(
        step: Double, y: Double, f: Double, stages: DoubleArray, stagesY: Double,
        stagesF: Double
    ): Double {
        return step * stagesF
    }
}
