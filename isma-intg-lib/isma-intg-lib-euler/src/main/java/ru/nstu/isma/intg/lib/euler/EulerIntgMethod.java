package ru.nstu.isma.intg.lib.euler;

import ru.nstu.isma.intg.api.methods.AccuracyIntgController;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.api.methods.StabilityIntgController;
import ru.nstu.isma.intg.api.methods.StageCalculator;

/**
 * @author Mariya Nasyrova
 * @since 04.10.14
 */
public class EulerIntgMethod implements IntgMethod {

    @Override public String getName() { return "Euler"; }

    @Override public AccuracyIntgController getAccuracyController() {
        return null;
    }
    @Override public StabilityIntgController getStabilityController() {
        return null;
    }
    @Override public StageCalculator[] getStageCalculators() {
        return new StageCalculator[0];
    }

    @Override public double nextY(double step, double[] k, double y, double f) {
        return y + step * f;
    }

}
