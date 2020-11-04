package ru.nstu.isma.intg.lib.rungeKutta.rk31.internal;

import ru.nstu.isma.intg.api.methods.StageCalculator;

/**
 * @author Dmitry Dostovalov
 * @since 16.10.15
 */

public class Rk31Stage4Calculator extends StageCalculator {
    @Override
    public double yk(double step, double y, double f, double[] stages) {
        return y + 1351.0/1024.0 * stages[0] - 525.0/1024.0 * stages[1] + 35.0/512.0 * stages[2];
    }

    @Override
    public double k(double step, double y, double f, double[] stages, double stagesY, double stagesF) {
        return step * stagesF;
    }
}
