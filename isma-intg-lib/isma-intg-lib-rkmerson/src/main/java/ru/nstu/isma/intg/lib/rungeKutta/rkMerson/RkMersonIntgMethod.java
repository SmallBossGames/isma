package ru.nstu.isma.intg.lib.rungeKutta.rkMerson;

import ru.nstu.isma.intg.api.methods.AccuracyIntgController;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.api.methods.StabilityIntgController;
import ru.nstu.isma.intg.api.methods.StageCalculator;
import ru.nstu.isma.intg.lib.rungeKutta.rkMerson.internal.*;

/**
 * @author Mariya Nasyrova
 * @since 04.10.14
 */
public class RkMersonIntgMethod implements IntgMethod {

    private final AccuracyIntgController accController;
    private final StageCalculator[] stageCalcs;

    public RkMersonIntgMethod() {
        accController = new RkMersonAccuracyIntgController();

        stageCalcs = new StageCalculator[5];
        stageCalcs[0] = new RkMersonStage1Calculator();
        stageCalcs[1] = new RkMersonStage2Calculator();
        stageCalcs[2] = new RkMersonStage3Calculator();
        stageCalcs[3] = new RkMersonStage4Calculator();
        stageCalcs[4] = new RkMersonStage5Calculator();
    }

    @Override public String getName() { return "Runge-Kutta-Merson"; }

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
        return y + (k[0] + 4.0 * k[3] + k[4]) / 6.0;
    }

}
