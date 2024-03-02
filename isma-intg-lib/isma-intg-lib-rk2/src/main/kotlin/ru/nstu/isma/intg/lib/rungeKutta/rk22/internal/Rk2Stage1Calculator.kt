package ru.nstu.isma.intg.lib.rungeKutta.rk22.internal

import ru.nstu.isma.intg.api.methods.StageCalculator

/**
 * @author Mariya Nasyrova
 * @since 04.10.14
 */
class Rk2Stage1Calculator : StageCalculator() {
    override fun yk(step: Double, y: Double, f: Double, stages: DoubleArray): Double {
        return y
    }

    override fun k(step: Double, y: Double, f: Double, stages: DoubleArray, stagesY: Double, stagesF: Double): Double {
        return step * f
    }
}
