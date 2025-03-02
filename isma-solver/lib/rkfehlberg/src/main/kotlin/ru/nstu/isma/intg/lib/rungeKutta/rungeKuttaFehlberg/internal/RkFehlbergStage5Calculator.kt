package ru.nstu.isma.intg.lib.rungeKutta.rungeKuttaFehlberg.internal

import ru.nstu.isma.intg.api.methods.StageCalculator

class RkFehlbergStage5Calculator : StageCalculator() {
    override fun yk(step: Double, y: Double, f: Double, stages: DoubleArray): Double {
        return y + 2.032407407 * stages[0] - 8.0 * stages[1] + 7.1734892788 * stages[2] - 0.2058966862 * stages[3]
    }

    override fun k(
        step: Double, y: Double, f: Double, stages: DoubleArray, stagesY: Double,
        stagesF: Double
    ): Double {
        return step * stagesF
    }
}
