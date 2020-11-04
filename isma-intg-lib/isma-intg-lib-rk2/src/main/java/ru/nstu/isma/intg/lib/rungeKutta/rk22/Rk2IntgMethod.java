package ru.nstu.isma.intg.lib.rungeKutta.rk22;

import ru.nstu.isma.intg.api.methods.AccuracyIntgController;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.api.methods.StabilityIntgController;
import ru.nstu.isma.intg.api.methods.StageCalculator;
import ru.nstu.isma.intg.lib.rungeKutta.rk22.internal.Rk2AccuracyIntgController;
import ru.nstu.isma.intg.lib.rungeKutta.rk22.internal.Rk2StabilityIntgController;
import ru.nstu.isma.intg.lib.rungeKutta.rk22.internal.Rk2Stage1Calculator;
import ru.nstu.isma.intg.lib.rungeKutta.rk22.internal.Rk2Stage2Calculator;

/**
 * @author Mariya Nasyrova
 * @since 01.09.14
 */

public class Rk2IntgMethod implements IntgMethod {
    private final AccuracyIntgController accController;
    private final StabilityIntgController stController;
    private final StageCalculator[] stageCalcs;

    public Rk2IntgMethod() {
        accController = new Rk2AccuracyIntgController();
        stController = new Rk2StabilityIntgController();

        stageCalcs = new StageCalculator[2];
        stageCalcs[0] = new Rk2Stage1Calculator();
        stageCalcs[1] = new Rk2Stage2Calculator();
    }

    @Override public String getName() { return "Runge-Kutta 2"; }

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
        return y + 0.5 * k[0] + 0.5 * k[1];

        // Результат, как по Эйлеру 1-ый порядок точности
        //return y + 0.25 * k[0] + 0.75 * k[1];
    }
}
