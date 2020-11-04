package ru.nstu.isma.intg.lib.rungeKutta.rkMerson.internal;

import ru.nstu.isma.intg.api.methods.StageCalculator;

/**
 * @author Mariya Nasyrova
 * @since 04.10.14
 */
public class RkMersonStage4Calculator extends StageCalculator {

    @Override
    public double yk(double step, double y, double f, double[] stages) {
        return y + 1.0/8.0 * stages[0] + 3.0/8.0 * stages[2];
    }

    @Override
    public double k(double step, double y, double f, double[] stages, double stagesY, double stagesF) {
        return step * stagesF;
    }

}
