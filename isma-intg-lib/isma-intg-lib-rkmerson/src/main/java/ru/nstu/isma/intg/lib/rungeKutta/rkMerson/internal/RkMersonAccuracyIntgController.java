package ru.nstu.isma.intg.lib.rungeKutta.rkMerson.internal;

import ru.nstu.isma.intg.core.methods.BaseAccuracyIntgController;

/**
 * @author Mariya Nasyrova
 * @since 04.10.14
 */
public class RkMersonAccuracyIntgController extends BaseAccuracyIntgController {

    @Override
    protected double getLocalErrorEstimation(double[] deStages) {
        return (2.0 * deStages[0] - 9.0 * deStages[2] + 8.0 * deStages[3] - deStages[4]) / 30.0;
    }

    @Override
    public double getActualAccuracy(double step, double localError) {
        return 0;
    }

    @Override
    public double getQ(double accuracy, double actualAccuracy) {
        return 1.0; // TODO: ???
    }

}
