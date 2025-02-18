package ru.nstu.isma.intg.lib.rungeKutta.rk3.internal

import ru.nstu.isma.intg.api.methods.IntgPoint
import ru.nstu.isma.intg.core.methods.BaseStabilityIntgController
import ru.nstu.isma.intg.core.methods.utils.maxOrThrow
import kotlin.math.abs

/**
 * @author Mariya Nasyrova
 * @since 04.10.14
 */
class Rk3StabilityIntgController : BaseStabilityIntgController() {
    public override fun getStabilityInterval(): Double {
        return STABILITY_INTERVAL
    }

    public override fun getMaxJacobiEigenValue(point: IntgPoint): Double {
        // TODO: в y передаются алгебраические переменные, поэтому ориентируемся по размеру стадий, поскольку они созда.тся только для оду
        val varCount = point.stages.size
        val deltaKs = DoubleArray(varCount)

        for (i in 0 until varCount) {
            val k1 = point.stages[i][0]
            val k2 = point.stages[i][1]
            val k3 = point.stages[i][2]

            if (k2 == k1) deltaKs[i] = 0.0 // TODO: проверить корректность, может поэтому залипает?
            else deltaKs[i] = abs(k1 - 2.0 * k2 + k3) / abs(k2 - k1)
        }

        val maxDeltaK = deltaKs.maxOrThrow()
        return 0.5 * maxDeltaK
    }

    companion object {
        private const val STABILITY_INTERVAL = 2.5
    }
}
