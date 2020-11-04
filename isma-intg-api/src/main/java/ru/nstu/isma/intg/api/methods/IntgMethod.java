package ru.nstu.isma.intg.api.methods;


import java.io.Serializable;

/**
 * @author Mariya Nasyrova
 * @since 30.08.14
 */
public interface IntgMethod extends Serializable {

    String getName();

    double nextY(double step, double[] k, double y, double f);

    StageCalculator[] getStageCalculators();

    AccuracyIntgController getAccuracyController();

    StabilityIntgController getStabilityController();

}