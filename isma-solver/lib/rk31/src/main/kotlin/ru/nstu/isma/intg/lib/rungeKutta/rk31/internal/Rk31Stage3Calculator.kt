package ru.nstu.isma.intg.lib.rungeKutta.rk31.internal

import ru.nstu.isma.intg.api.methods.StageCalculator

/**
 * @author Dmitry Dostovalov
 * @since 16.10.15
 */
class Rk31Stage3Calculator : StageCalculator() {
    override fun yk(step: Double, y: Double, f: Double, stages: DoubleArray): Double {
        return y + 11.0 / 8.0 * stages[0] - 3.0 / 8.0 * stages[1]
    }

    override fun k(step: Double, y: Double, f: Double, stages: DoubleArray, stagesY: Double, stagesF: Double): Double {
        return step * stagesF
    }
}
