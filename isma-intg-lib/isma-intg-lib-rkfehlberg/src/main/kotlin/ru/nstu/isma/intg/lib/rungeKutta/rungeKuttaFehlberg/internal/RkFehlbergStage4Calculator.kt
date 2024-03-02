package ru.nstu.isma.intg.lib.rungeKutta.rungeKuttaFehlberg.internal

import ru.nstu.isma.intg.api.methods.StageCalculator

class RkFehlbergStage4Calculator : StageCalculator() {
    override fun yk(step: Double, y: Double, f: Double, stages: DoubleArray): Double {
        return y + 0.87938097 * stages[0] - 3.2771961766 * stages[1] + 3.320892126 * stages[2]
    }

    override fun k(
        step: Double, y: Double, f: Double, stages: DoubleArray, stagesY: Double,
        stagesF: Double
    ): Double {
        return step * stagesF
    }
}
