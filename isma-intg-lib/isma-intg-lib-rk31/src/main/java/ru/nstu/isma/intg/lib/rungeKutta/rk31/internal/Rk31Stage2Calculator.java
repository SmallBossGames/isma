package ru.nstu.isma.intg.lib.rungeKutta.rk31.internal;

import ru.nstu.isma.intg.api.methods.StageCalculator;

/**
 * @author Dmitry Dostovalov
 * @since 16.10.15
 */

public class Rk31Stage2Calculator extends StageCalculator {
    @Override
    public double yk(double step, double y, double f, double[] stages) {
        return y + 2.0/3.0 * stages[0];
    }

    @Override
    public double k(double step, double y, double f, double[] stages, double stagesY, double stagesF) {
        return step * stagesF;
    }
}
