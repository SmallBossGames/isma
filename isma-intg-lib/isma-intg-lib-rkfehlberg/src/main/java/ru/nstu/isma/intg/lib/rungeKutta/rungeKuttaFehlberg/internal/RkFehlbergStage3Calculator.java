package ru.nstu.isma.intg.lib.rungeKutta.rungeKuttaFehlberg.internal;

import ru.nstu.isma.intg.api.methods.*;

public class RkFehlbergStage3Calculator extends StageCalculator {
	@Override
	public double yk(final double step, final double y, final double f, final double[] stages) {
		return y + 0.09375 * stages[0] + 0.28125 * stages[1];
	}

	@Override
	public double k(final double step, final double y, final double f, final double[] stages, final double stagesY,
			final double stagesF) {
		return step * stagesF;
	}
}
