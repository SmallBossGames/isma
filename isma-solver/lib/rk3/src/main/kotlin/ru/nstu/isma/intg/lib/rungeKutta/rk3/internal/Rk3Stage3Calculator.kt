package ru.nstu.isma.intg.lib.rungeKutta.rk3.internal

import ru.nstu.isma.intg.api.methods.StageCalculator

/**
 * @author Mariya Nasyrova
 * @since 04.10.14
 */
class Rk3Stage3Calculator : StageCalculator() {
    override fun yk(step: Double, y: Double, f: Double, stages: DoubleArray): Double {
        return y - stages[0] + 2.0 * stages[1]
    }

    override fun k(step: Double, y: Double, f: Double, stages: DoubleArray, stagesY: Double, stagesF: Double): Double {
        return step * stagesF
    }
}
