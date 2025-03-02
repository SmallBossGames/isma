package ru.nstu.isma.intg.lib.rungeKutta.rungeKuttaFehlberg.internal

import ru.nstu.isma.intg.api.methods.StageCalculator

class RkFehlbergStage6Calculator : StageCalculator() {
    override fun yk(step: Double, y: Double, f: Double, stages: DoubleArray): Double {
        return (y - 0.2962962962 * stages[0] + 2.0 * stages[1] - 1.3816764133 * stages[2] + 0.4529727096 * stages[3]
                - 0.275 * stages[4])
    }

    override fun k(
        step: Double, y: Double, f: Double, stages: DoubleArray, stagesY: Double,
        stagesF: Double
    ): Double {
        return step * stagesF
    }
}
