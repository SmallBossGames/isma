package ru.nstu.isma.intg.lib.rungeKutta.rungeKuttaFehlberg;

import ru.nstu.isma.intg.lib.rungeKutta.rungeKuttaFehlberg.internal.*;
import ru.nstu.isma.intg.api.methods.*;

public class RkFehlbergIntgMethod implements IntgMethod {
	private final RkFehlbergAccuracyIntgController accController;
	private final StabilityIntgController stController;
	private final StageCalculator[] stageCalcs;

	public RkFehlbergIntgMethod() {
		this.accController = new RkFehlbergAccuracyIntgController();
		(this.stageCalcs = new StageCalculator[6])[0] = new RkFehlbergStage1Calculator();
		this.stageCalcs[1] = new RkFehlbergStage2Calculator();
		this.stageCalcs[2] = new RkFehlbergStage3Calculator();
		this.stageCalcs[3] = new RkFehlbergStage4Calculator();
		this.stageCalcs[4] = new RkFehlbergStage5Calculator();
		this.stageCalcs[5] = new RkFehlbergStage6Calculator();
		this.stController = new RkFehlbergStabilityIntgController();
	}

	@Override
	public String getName() {
		return "Runge-Kutta-Fehlberg";
	}

	@Override
	public AccuracyIntgController getAccuracyController() {
		return this.accController;
	}

	@Override
	public StabilityIntgController getStabilityController() {
		return this.stController;
	}

	@Override
	public StageCalculator[] getStageCalculators() {
		return this.stageCalcs;
	}

	@Override
	public double nextY(final double step, final double[] k, final double y, final double f) {
		return y + 0.1185185185D * k[0] + 0.5189863548D * k[2] + 0.5061314903D * k[3] - 0.18 * k[4]
				+ 0.0363636363D * k[5];
	}
}
