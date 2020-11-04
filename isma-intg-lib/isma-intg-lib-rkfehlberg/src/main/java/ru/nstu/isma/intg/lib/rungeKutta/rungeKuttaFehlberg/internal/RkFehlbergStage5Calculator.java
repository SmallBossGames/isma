package ru.nstu.isma.intg.lib.rungeKutta.rungeKuttaFehlberg.internal;

import ru.nstu.isma.intg.api.methods.*;

public class RkFehlbergStage5Calculator extends StageCalculator {
	@Override
	public double yk(final double step, final double y, final double f, final double[] stages) {
		return y + 2.032407407 * stages[0] - 8.0 * stages[1] + 7.1734892788 * stages[2] - 0.2058966862 * stages[3];
	}

	@Override
	public double k(final double step, final double y, final double f, final double[] stages, final double stagesY,
			final double stagesF) {
		return step * stagesF;
	}
}
