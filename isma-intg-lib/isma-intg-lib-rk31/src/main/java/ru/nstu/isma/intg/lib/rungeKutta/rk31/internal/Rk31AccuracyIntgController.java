package ru.nstu.isma.intg.lib.rungeKutta.rk31.internal;

import ru.nstu.isma.intg.core.methods.BaseAccuracyIntgController;

/**
 * @author Dmitry Dostovalov
 * @since 16.10.15
 */

public class Rk31AccuracyIntgController extends BaseAccuracyIntgController {
    @Override
    protected double getLocalErrorEstimation(double[] deStages) {
        return (17.0/84.0 - 0.25) * deStages[0] + (27.0/20.0 - 0.75) * deStages[1] + 2.0/3.0 * deStages[2] - 128.0/105.0 * deStages[3];
    }

    @Override
    public double getActualAccuracy(double step, double localError) {
        return 8.0 * localError; // TODO: проверить
    }

    @Override
    public double getQ(double accuracy, double actualAccuracy) {
        if (actualAccuracy == 0) { return 1; }
        double q = Math.pow(accuracy / actualAccuracy, 1.0 / 3.0);
        return q;
    }
}
