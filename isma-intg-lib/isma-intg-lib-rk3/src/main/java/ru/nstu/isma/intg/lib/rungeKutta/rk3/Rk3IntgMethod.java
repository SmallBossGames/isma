package ru.nstu.isma.intg.lib.rungeKutta.rk3;

import ru.nstu.isma.intg.api.methods.AccuracyIntgController;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.api.methods.StabilityIntgController;
import ru.nstu.isma.intg.api.methods.StageCalculator;
import ru.nstu.isma.intg.lib.rungeKutta.rk3.internal.*;

/**
 * @author Mariya Nasyrova
 * @since 04.10.14
 */
public class Rk3IntgMethod implements IntgMethod {

    private final AccuracyIntgController accController;
    private final StabilityIntgController stController;
    private final StageCalculator[] stageCalcs;

    public Rk3IntgMethod() {
        accController = new Rk3AccuracyIntgController();
        stController = new Rk3StabilityIntgController();

        stageCalcs = new StageCalculator[3];
        stageCalcs[0] = new Rk3Stage1Calculator();
        stageCalcs[1] = new Rk3Stage2Calculator();
        stageCalcs[2] = new Rk3Stage3Calculator();
    }

    @Override public String getName() { return "Runge-Kutta 3"; }

    @Override public AccuracyIntgController getAccuracyController() {
        return accController;
    }
    @Override public StabilityIntgController getStabilityController() {
        return stController;
    }
    @Override public StageCalculator[] getStageCalculators() {
        return stageCalcs;
    }

    @Override public double nextY(double step, double[] k, double y, double f) {
        return y + 1.0/6.0 * k[0] + 2.0/3.0 * k[1] + 1.0/6.0 * k[2];
    }

}
