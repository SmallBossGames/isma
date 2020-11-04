package ru.nstu.isma.intg.api;

import ru.nstu.isma.intg.api.calcmodel.DaeSystem;

import java.util.Arrays;

/**
 * @author Maria
 * @since 28.03.2016
 */
public class IntgResultPoint {

    private final double x;
    private final double[] yForDe;
    private final double[][] rhs; // TODO: можно сохранять только алгебраические

    public IntgResultPoint(double x, double[] yForDe, double[][] rhs) {
        this.x = x;
        this.yForDe = yForDe;
        this.rhs = rhs;
    }

    public double getX() {
        return x;
    }

    public double[] getYForDe() {
        return yForDe;
    }

    public double[][] getRhs() {
        return rhs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntgResultPoint that = (IntgResultPoint) o;

        if (Double.compare(that.x, x) != 0) return false;
        if (!Arrays.equals(yForDe, that.yForDe)) return false;
        return Arrays.equals(rhs[DaeSystem.RHS_AE_PART_IDX], that.rhs[DaeSystem.RHS_AE_PART_IDX]);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + Arrays.hashCode(yForDe);
        result = 31 * result + Arrays.hashCode(rhs[DaeSystem.RHS_AE_PART_IDX]);
        return result;
    }
}
