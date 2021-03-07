package ru.nstu.isma.hsm.common;

/**
 * Created by Bessonov Alex
 * on 13.03.2015.
 */
public interface Calculateable {
    void prepareForCalculation(IndexMapper indexMapper);

    double[] calculate(double[] y);
}
