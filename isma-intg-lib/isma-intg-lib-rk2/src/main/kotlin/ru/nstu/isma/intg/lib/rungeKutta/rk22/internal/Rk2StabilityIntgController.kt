package ru.nstu.isma.intg.lib.rungeKutta.rk22.internal

import ru.nstu.isma.intg.api.calcmodel.DaeSystem
import ru.nstu.isma.intg.api.methods.IntgPoint
import ru.nstu.isma.intg.core.methods.BaseStabilityIntgController
import ru.nstu.isma.intg.core.methods.utils.MathUtils
import kotlin.math.abs

/**
 * @author Mariya Nasyrova
 * @since 29.08.14
 */
class Rk2StabilityIntgController : BaseStabilityIntgController() {
    public override fun getStabilityInterval(): Double {
        return STABILITY_INTERVAL
    }

    public override fun getMaxJacobiEigenValue(point: IntgPoint): Double {
        val varCount = point.stages.size
        val deltaKs = DoubleArray(varCount)
        var k1: Double
        var k2: Double

        for (i in 0 until varCount) {
            k1 = point.stages[i][0]
            k2 = point.stages[i][1]

            if (k2 == k1) {
                deltaKs[i] = 0.0
            } else {
                deltaKs[i] = abs(point.rhs[DaeSystem.RHS_DE_PART_IDX][i] - k2) / abs(k2 - k1)
            }
        }

        return 2.0 * MathUtils.max(deltaKs)
    }

    companion object {
        private const val STABILITY_INTERVAL = 2.0
    }
}
