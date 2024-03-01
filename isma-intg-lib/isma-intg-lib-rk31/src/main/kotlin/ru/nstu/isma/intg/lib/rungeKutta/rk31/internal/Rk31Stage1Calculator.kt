package ru.nstu.isma.intg.lib.rungeKutta.rk31.internal

import ru.nstu.isma.intg.api.methods.StageCalculator

/**
 * @author Dmitry Dostovalov
 * @since 16.10.15
 */
class Rk31Stage1Calculator : StageCalculator() {
    override fun yk(step: Double, y: Double, f: Double, stages: DoubleArray): Double {
        return y
    }

    override fun k(step: Double, y: Double, f: Double, stages: DoubleArray, stagesY: Double, stagesF: Double): Double {
        return step * f
    }
}
