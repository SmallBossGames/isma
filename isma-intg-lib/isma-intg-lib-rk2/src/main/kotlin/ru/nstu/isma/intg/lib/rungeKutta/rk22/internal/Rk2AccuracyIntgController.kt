package ru.nstu.isma.intg.lib.rungeKutta.rk22.internal

import ru.nstu.isma.intg.core.methods.BaseAccuracyIntgController
import kotlin.math.sqrt

/**
 * @author Mariya Nasyrova
 * @since 20.08.14
 */
class Rk2AccuracyIntgController : BaseAccuracyIntgController() {
    override fun getLocalErrorEstimation(deStages: DoubleArray): Double {
        return 0.5 * (deStages[1] - deStages[0])
    }

    public override fun getActualAccuracy(step: Double, localError: Double): Double {
        return step * localError
    }

    public override fun getQ(accuracy: Double, actualAccuracy: Double): Double {
        if (actualAccuracy == 0.0) {
            return 1.0
        }
        return sqrt(accuracy / actualAccuracy)
    }
}