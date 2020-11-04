package ru.nstu.isma.intg.lib.rungeKutta.rk3.internal;

import ru.nstu.isma.intg.api.methods.StageCalculator;

/**
 * @author Mariya Nasyrova
 * @since 04.10.14
 */
public class Rk3Stage3Calculator extends StageCalculator {

    @Override
    public double yk(double step, double y, double f, double[] stages) {
        return y - stages[0] + 2.0 * stages[1];
    }

    @Override
    public double k(double step, double y, double f, double[] stages, double stagesY, double stagesF) {
        return step * stagesF;
    }

}
