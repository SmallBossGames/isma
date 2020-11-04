package ru.nstu.isma.intg.lib.rungeKutta.rungeKuttaFehlberg.internal;

import ru.nstu.isma.intg.core.methods.*;
import ru.nstu.isma.intg.api.methods.*;
import ru.nstu.isma.intg.core.methods.utils.*;

public class RkFehlbergStabilityIntgController extends BaseStabilityIntgController {
	private static final double STABILITY_INTERVAL = 3.6;

	public double getStabilityInterval() {
		return 3.6;
	}

	public double getMaxJacobiEigenValue(final IntgPoint point) {
		final int varCount = point.getStages().length;
		final double[] deltaKs = new double[varCount];
		for (int i = 0; i < varCount; ++i) {
			final double k1 = point.getStages()[i][0];
			final double k2 = point.getStages()[i][1];
			final double k3 = point.getStages()[i][2];
			if (k2 == k1) {
				deltaKs[i] = 0.0;
			} else {
				deltaKs[i] = Math.abs(32.0 * k3 - 48.0 * k2 + 16.0 * k1) / Math.abs(k2 - k1);
			}
		}
		return MathUtils.max(deltaKs) / 9;
	}
}
