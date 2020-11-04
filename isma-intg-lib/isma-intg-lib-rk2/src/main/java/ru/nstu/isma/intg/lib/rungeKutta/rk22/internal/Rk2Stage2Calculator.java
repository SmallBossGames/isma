package ru.nstu.isma.intg.lib.rungeKutta.rk22.internal;

import ru.nstu.isma.intg.api.methods.StageCalculator;

/**
 * @author Mariya Nasyrova
 * @since 04.10.14
 */
public class Rk2Stage2Calculator extends StageCalculator {
    @Override public double yk(double step, double y, double f, double[] k) {
        return y + k[0];
    }
    @Override public double k(double step, double y, double f, double[] k, double yk, double fk) {
        return step * fk;
    }
}
