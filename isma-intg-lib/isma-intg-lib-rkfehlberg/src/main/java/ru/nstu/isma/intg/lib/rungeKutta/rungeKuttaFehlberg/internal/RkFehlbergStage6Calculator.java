package ru.nstu.isma.intg.lib.rungeKutta.rungeKuttaFehlberg.internal;

import ru.nstu.isma.intg.api.methods.*;

public class RkFehlbergStage6Calculator extends StageCalculator {
	@Override
	public double yk(final double step, final double y, final double f, final double[] stages) {
		return y - 0.2962962962 * stages[0] + 2.0 * stages[1] - 1.3816764133 * stages[2] + 0.4529727096 * stages[3]
				- 0.275 * stages[4];
	}

	@Override
	public double k(final double step, final double y, final double f, final double[] stages, final double stagesY,
			final double stagesF) {
		return step * stagesF;
	}
}
