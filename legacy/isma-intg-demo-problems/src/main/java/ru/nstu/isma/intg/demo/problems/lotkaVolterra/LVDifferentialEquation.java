package ru.nstu.isma.intg.demo.problems.lotkaVolterra;

import ru.nstu.isma.intg.api.calcmodel.DifferentialEquation;

public class LVDifferentialEquation extends DifferentialEquation {

    private static final long serialVersionUID = 0L;

    private double d = 0;

    private double stepOnX = 0;
    private double stepOnZ = 0;

    private LVFunction f;

    private int jNextK = 0;
    private int jPrevK = 0;
    private int jK = 0;
    private int jKNext = 0;
    private int jKPrev = 0;
    private int jK1 = 0;
    private int jK2 = 0;

    public LVDifferentialEquation(int index) {
        super(index, null);
        this.setFunction(this::calculateRightMember);
    }

    private double calculateRightMember(double[] y, double[][] rhs) {
        // Возведение в степень с помощью Math.pow в 300-600 раз медленнее, поэтому делаем так.
        return d / (stepOnX * stepOnX)
                * (y[jNextK] - 2 * y[jK] + y[jPrevK])
                + d / (stepOnZ * stepOnZ)
                * (y[jKNext] - 2 * y[jK] + y[jKPrev])
                + f.calculate(y[jK1], y[jK2]);
    }

    public double getD() {
        return d;
    }

    public double getStepOnX() {
        return stepOnX;
    }

    public double getStepOnZ() {
        return stepOnZ;
    }

    public LVFunction getF() {
        return f;
    }

    public int getjNextK() {
        return jNextK;
    }

    public int getjPrevK() {
        return jPrevK;
    }

    public int getjK() {
        return jK;
    }

    public int getjKNext() {
        return jKNext;
    }

    public int getjKPrev() {
        return jKPrev;
    }

    public int getjK1() {
        return jK1;
    }

    public int getjK2() {
        return jK2;
    }

    public void setD(double d) {
        this.d = d;
    }

    public void setStepOnX(double stepOnX) {
        this.stepOnX = stepOnX;
    }

    public void setStepOnZ(double stepOnZ) {
        this.stepOnZ = stepOnZ;
    }

    public void setF(LVFunction f) {
        this.f = f;
    }

    public void setIndexes(int jNextK, int jPrevK, int jK, int jKNext, int jKPrev, int jK1, int jK2) {
        this.jNextK = jNextK;
        this.jPrevK = jPrevK;
        this.jK = jK;
        this.jKNext = jKNext;
        this.jKPrev = jKPrev;
        this.jK1 = jK1;
        this.jK2 = jK2;
    }

    @Override
    public String getDescription() {
        return d + " / (" + stepOnX + "^2) " +
                "* (y" + jNextK + " - 2" + "y" + jK + " + y" + jPrevK + ") " +
                "+ " + d + " / (" + stepOnZ + "^2) " +
                "* (y" + jKNext + " - 2" + "y" + jK + " + y" + jKPrev + ") " +
                "+ f(y" + jK1 + ", y" + jK2 + ")";
    }
}