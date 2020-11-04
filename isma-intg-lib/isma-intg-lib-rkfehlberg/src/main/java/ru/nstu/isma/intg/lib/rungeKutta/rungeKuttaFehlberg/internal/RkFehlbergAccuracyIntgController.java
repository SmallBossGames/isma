package ru.nstu.isma.intg.lib.rungeKutta.rungeKuttaFehlberg.internal;

import ru.nstu.isma.intg.core.methods.*;

public class RkFehlbergAccuracyIntgController extends BaseAccuracyIntgController {
	@Override
	protected double getLocalErrorEstimation(final double[] deStages) {

		final double[] p4 = { 1.5625, 0.0, 0.5489278752, 0.5353313840, -0.2, 0 };
		final double[] p5 = { 0.1185185185, 0.0, 0.5189863548, 0.5061314903, -0.18, 0.0363636363 };
		double delta = 0.0;
		for (int i = 0; i < 6; i++) {
			delta = delta + deStages[i] * (p5[i] - p4[i]);
		}
		return 17.0 * delta / 24.0;
	}

	public double getActualAccuracy(final double step, final double localError) {
		return 0.0;
	}

	public double getQ(final double accuracy, final double actualAccuracy) {
		return 1.0;
	}
}
