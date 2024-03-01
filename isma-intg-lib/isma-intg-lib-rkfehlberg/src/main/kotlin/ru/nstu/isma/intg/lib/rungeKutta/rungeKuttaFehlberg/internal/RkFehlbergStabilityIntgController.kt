package ru.nstu.isma.intg.lib.rungeKutta.rungeKuttaFehlberg.internal

import ru.nstu.isma.intg.api.methods.IntgPoint
import ru.nstu.isma.intg.core.methods.BaseStabilityIntgController
import ru.nstu.isma.intg.core.methods.utils.MathUtils
import kotlin.math.abs

class RkFehlbergStabilityIntgController : BaseStabilityIntgController() {
    public override fun getStabilityInterval(): Double {
        return 3.6
    }

    public override fun getMaxJacobiEigenValue(point: IntgPoint): Double {
        val varCount = point.stages.size
        val deltaKs = DoubleArray(varCount)
        for (i in 0 until varCount) {
            val k1 = point.stages[i][0]
            val k2 = point.stages[i][1]
            val k3 = point.stages[i][2]
            if (k2 == k1) {
                deltaKs[i] = 0.0
            } else {
                deltaKs[i] = abs(32.0 * k3 - 48.0 * k2 + 16.0 * k1) / abs(k2 - k1)
            }
        }
        return MathUtils.max(deltaKs) / 9
    }

    companion object {
        private const val STABILITY_INTERVAL = 3.6
    }
}
