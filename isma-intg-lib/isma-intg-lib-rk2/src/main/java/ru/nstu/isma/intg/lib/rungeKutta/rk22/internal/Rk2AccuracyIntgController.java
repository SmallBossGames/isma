package ru.nstu.isma.intg.lib.rungeKutta.rk22.internal;

import ru.nstu.isma.intg.core.methods.BaseAccuracyIntgController;

/**
 * @author Mariya Nasyrova
 * @since 20.08.14
 */


public class Rk2AccuracyIntgController extends BaseAccuracyIntgController {
    @Override protected double getLocalErrorEstimation(double[] k) {
        return 0.5 * (k[1] - k[0]);
    }
    @Override public double getActualAccuracy(double step, double localError) {
		return step * localError;
	}
	@Override public double getQ(double accuracy, double actualAccuracy) {
		if (actualAccuracy == 0) {
			return 1.0;
		}
		return Math.sqrt(accuracy / actualAccuracy);
	}
}