package ru.nstu.isma.intg.api.calcmodel.cauchy;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Начальные условия задачи Коши
 */
public class CauchyInitialsLegacy implements Serializable {

    private double start = 0.0;
    private double end = 0.0;
    private double[] y0 = new double[0];
    private double stepSize = 0.0;

    /**
     * Возвращает начало отрезка Пеано (начало интервала моделирования).
     */
    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    /**
     * Возвращает конец отрезка Пеано (конец интервала моделирования).
     */
    public double getEnd() {
        return end;
    }

    public void setEnd(double end) {
        this.end = end;
    }

    public void setInterval(double intervalStart, double intervalEnd) {
        this.start = intervalStart;
        this.end = intervalEnd;
    }

    /**
     * Возвращает приближенное решение в начальный момент времени.
     */
    public double[] getY0() {
        return y0;
    }

    public void setY0(double[] y0) {
        this.y0 = y0;
    }

    /**
     * Возвращает первоначальную величину шага интегрирования.
     */
    public double getStepSize() {
        return stepSize;
    }

    public void setStepSize(double stepSize) {
        this.stepSize = stepSize;
    }

    @Override
    public String toString() {
        return "CauchyInitials{" +
                "start=" + start +
                ", end=" + end +
                ", y0=" + Arrays.toString(y0) +
                ", stepSize=" + stepSize +
                '}';
    }
}