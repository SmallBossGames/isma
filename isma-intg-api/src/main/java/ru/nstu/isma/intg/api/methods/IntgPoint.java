package ru.nstu.isma.intg.api.methods;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Точка интегрирования.
 * Содержит решение, вычисленное для текущего шага, и размер следующего шага интегрирования.
 *
 * @author Maria Nasyrova
 * @since 28.08.2015
 */
public class IntgPoint implements Serializable {

    /** Шаг интегрирования. */
    private double step;

    /** Значение y, соответствующие step. */
    private double[] y;

    /** Массив значений правой части системы уравнений, вычисленный по y. */
    private double[][] rhs;

    /** Значения вспомогательных стадий метода, использовавшихся для вычисления y. */
    private double[][] stages;

    /** Величина следующего шага интегрирования. */
    private double nextStep;

    public IntgPoint(double step, double[] y, double[][] rhs) {
        this(step, y, rhs, new double[0][], step);
    }

    public IntgPoint(double step, double[] y, double[][] rhs, double[][] stages, double nextStep) {
        this.step = step;
        this.y = y;
        this.rhs = rhs;
        this.stages = stages;
        this.nextStep = nextStep;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public double[] getY() {
        return y;
    }

    public void setY(double[] y) {
        this.y = y;
    }

    public double[][] getStages() {
        return stages;
    }

    public void setStages(double[][] stages) {
        this.stages = stages;
    }

    public double[][] getRhs() {
        return rhs;
    }

    public void setRhs(double[][] rhs) {
        this.rhs = rhs;
    }

    public double getNextStep() {
        return nextStep;
    }

    public void setNextStep(double nextStep) {
        this.nextStep = nextStep;
    }

    public IntgPoint copyLight() {
        double[] newY = Arrays.copyOf(y, y.length);
        double[][] newRhs = new double[rhs.length][];
        for (int i = 0; i < rhs.length; i++) {
            if (rhs[i] != null) {
                newRhs[i] = Arrays.copyOf(rhs[i], rhs[i].length);
            }
        }
        return new IntgPoint(step, newY, newRhs);
    }

}
