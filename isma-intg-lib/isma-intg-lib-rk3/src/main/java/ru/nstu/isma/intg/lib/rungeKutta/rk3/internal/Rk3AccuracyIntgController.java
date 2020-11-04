package ru.nstu.isma.intg.lib.rungeKutta.rk3.internal;

import ru.nstu.isma.intg.core.methods.BaseAccuracyIntgController;

/**
 * @author Mariya Nasyrova
 * @since 04.10.14
 */
public class Rk3AccuracyIntgController extends BaseAccuracyIntgController {

    @Override
    protected double getLocalErrorEstimation(double[] deStages) {
        return (deStages[0] - 2.0 * deStages[1] + deStages[2]) / 6.0;
    }

    @Override
    public double getActualAccuracy(double step, double localError) {
        return 0.5 * step * localError; // TODO: проверить, м.б. что-то другое, но вроде так;
    }

    @Override
    public double getQ(double accuracy, double actualAccuracy) {
        if (actualAccuracy == 0) { return 1; }
        double q = Math.pow(accuracy / actualAccuracy, 1.0 / 3.0);
        return q;
    }
}
