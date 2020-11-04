package ru.nstu.isma.intg.lib.rungeKutta.rk31;

import ru.nstu.isma.intg.api.methods.AccuracyIntgController;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.api.methods.StabilityIntgController;
import ru.nstu.isma.intg.api.methods.StageCalculator;
import ru.nstu.isma.intg.lib.rungeKutta.rk31.internal.*;

/**
 * @author Dmitry Dostovalov
 * @since 16.10.15
 */

public class Rk31IntgMethod implements IntgMethod {
    private final AccuracyIntgController accController;
    private final StageCalculator[] stageCalcs;

    public Rk31IntgMethod() {
        accController = new Rk31AccuracyIntgController();

        stageCalcs = new StageCalculator[4];
        stageCalcs[0] = new Rk31Stage1Calculator();
        stageCalcs[1] = new Rk31Stage2Calculator();
        stageCalcs[2] = new Rk31Stage3Calculator();
        stageCalcs[3] = new Rk31Stage4Calculator();
    }

    @Override public String getName() { return "Runge-Kutta 3-1"; }

    @Override public AccuracyIntgController getAccuracyController() {
        return accController;
    }
    @Override public StabilityIntgController getStabilityController() {
        return null;
    }
    @Override public StageCalculator[] getStageCalculators() {
        return stageCalcs;
    }

    @Override public double nextY(double step, double[] k, double y, double f) {
        return y + 17.0/84.0*k[0] + 27.0/20.0*k[1] + 2.0/3.0*k[2] - 128.0/105.0*k[3];
    }
}
