package ru.nstu.isma.intg.lib.rungeKutta.rk3.internal

import ru.nstu.isma.intg.core.methods.BaseAccuracyIntgController
import kotlin.math.pow

/**
 * @author Mariya Nasyrova
 * @since 04.10.14
 */
class Rk3AccuracyIntgController : BaseAccuracyIntgController() {
    override fun getLocalErrorEstimation(deStages: DoubleArray): Double {
        return (deStages[0] - 2.0 * deStages[1] + deStages[2]) / 6.0
    }

    public override fun getActualAccuracy(step: Double, localError: Double): Double {
        return 0.5 * step * localError // TODO: проверить, м.б. что-то другое, но вроде так;
    }

    public override fun getQ(accuracy: Double, actualAccuracy: Double): Double {
        if (actualAccuracy == 0.0) {
            return 1.0
        }
        val q: Double = (accuracy / actualAccuracy).pow(1.0 / 3.0)
        return q
    }
}
