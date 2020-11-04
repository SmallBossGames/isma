package ru.nstu.isma.intg.api.methods;

import java.io.Serializable;

/**
 * @author Mariya Nasyrova
 * @since 01.09.14
 */
public abstract class StageCalculator implements Serializable {

    public abstract double yk(double step, double y, double f, double[] stages);

    public abstract double k(double step, double y, double f, double[] stages, double stagesY, double stagesF);

}
