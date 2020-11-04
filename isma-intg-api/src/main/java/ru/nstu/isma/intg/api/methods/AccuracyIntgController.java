package ru.nstu.isma.intg.api.methods;

import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver;

import java.io.Serializable;

/**
 * @author Maria Nasyrova
 * @since 25.02.2015
 */
public abstract class AccuracyIntgController extends IntgController {

    private double accuracy;

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public abstract AccuracyResults tune(
            IntgPoint fromPoint, double[][] stages, DaeSystemStepSolver stepSolver);

    protected abstract double getLocalErrorEstimation(double[] deStages);

    protected abstract double getActualAccuracy(double step, double localError);

    protected abstract double getQ(double accuracy, double actualAccuracy);


    public static class AccuracyResults implements Serializable {
        private final double tunedStep;
        private final double[][] tunedStages;

        public AccuracyResults(double tunedStep, double[][] tunedStages) {
            this.tunedStep = tunedStep;
            this.tunedStages = tunedStages;
        }

        public double getTunedStep() {
            return tunedStep;
        }

        public double[][] getTunedStages() {
            return tunedStages;
        }
    }
}
