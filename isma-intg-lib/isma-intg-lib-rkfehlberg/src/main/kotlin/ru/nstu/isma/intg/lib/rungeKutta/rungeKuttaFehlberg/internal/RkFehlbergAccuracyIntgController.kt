package ru.nstu.isma.intg.lib.rungeKutta.rungeKuttaFehlberg.internal

import ru.nstu.isma.intg.core.methods.BaseAccuracyIntgController

class RkFehlbergAccuracyIntgController : BaseAccuracyIntgController() {
    override fun getLocalErrorEstimation(deStages: DoubleArray): Double {
        val p4 = doubleArrayOf(1.5625, 0.0, 0.5489278752, 0.5353313840, -0.2, 0.0)
        val p5 = doubleArrayOf(0.1185185185, 0.0, 0.5189863548, 0.5061314903, -0.18, 0.0363636363)
        var delta = 0.0
        for (i in 0..5) {
            delta += deStages[i] * (p5[i] - p4[i])
        }
        return 17.0 * delta / 24.0
    }

    public override fun getActualAccuracy(step: Double, localError: Double): Double {
        return 0.0
    }

    public override fun getQ(accuracy: Double, actualAccuracy: Double): Double {
        return 1.0
    }
}
