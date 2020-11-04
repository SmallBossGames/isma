package ru.nstu.isma.intg.lib.rungeKutta.rungeKuttaFehlberg.internal;

import ru.nstu.isma.intg.api.methods.*;

public class RkFehlbergStage1Calculator extends StageCalculator {
	@Override
	public double yk(final double step, final double y, final double f, final double[] k) {
		return y;
	}

	@Override
	public double k(final double step, final double y, final double f, final double[] k, final double yk,
			final double fk) {
		return step * f;
	}
}
