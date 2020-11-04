package ru.nstu.isma.intg.lib.rungeKutta.rkMerson.internal;

import ru.nstu.isma.intg.api.methods.StageCalculator;

/**
 * @author Mariya Nasyrova
 * @since 04.10.14
 */
public class RkMersonStage5Calculator extends StageCalculator {

    @Override
    public double yk(double step, double y, double f, double[] stages) {
        return y + 0.5 * stages[0] - 1.5 * stages[2] + 2.0 * stages[3];
    }

    @Override
    public double k(double step, double y, double f, double[] stages, double stagesY, double stagesF) {
        return step * stagesF;
    }

}
