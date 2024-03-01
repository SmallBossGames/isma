package ru.nstu.isma.intg.lib.rungeKutta.rkMerson.internal

import ru.nstu.isma.intg.core.methods.BaseAccuracyIntgController

/**
 * @author Mariya Nasyrova
 * @since 04.10.14
 */
class RkMersonAccuracyIntgController : BaseAccuracyIntgController() {
    override fun getLocalErrorEstimation(deStages: DoubleArray): Double {
        return (2.0 * deStages[0] - 9.0 * deStages[2] + 8.0 * deStages[3] - deStages[4]) / 30.0
    }

    public override fun getActualAccuracy(step: Double, localError: Double): Double {
        return 0.0
    }

    public override fun getQ(accuracy: Double, actualAccuracy: Double): Double {
        return 1.0 // TODO: ???
    }
}
