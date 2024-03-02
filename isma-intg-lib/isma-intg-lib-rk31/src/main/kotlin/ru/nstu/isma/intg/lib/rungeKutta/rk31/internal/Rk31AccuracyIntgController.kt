package ru.nstu.isma.intg.lib.rungeKutta.rk31.internal

import ru.nstu.isma.intg.core.methods.BaseAccuracyIntgController
import kotlin.math.pow

/**
 * @author Dmitry Dostovalov
 * @since 16.10.15
 */
class Rk31AccuracyIntgController : BaseAccuracyIntgController() {
    override fun getLocalErrorEstimation(deStages: DoubleArray): Double {
        return (17.0 / 84.0 - 0.25) * deStages[0] + ((27.0 / 20.0 - 0.75) * deStages[1]) + (2.0 / 3.0 * deStages[2]) - 128.0 / 105.0 * deStages[3]
    }

    public override fun getActualAccuracy(step: Double, localError: Double): Double {
        return 8.0 * localError // TODO: проверить
    }

    public override fun getQ(accuracy: Double, actualAccuracy: Double): Double {
        if (actualAccuracy == 0.0) {
            return 1.0
        }
        val q: Double = (accuracy / actualAccuracy).pow(1.0 / 3.0)
        return q
    }
}
