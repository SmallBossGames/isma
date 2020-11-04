package ru.nstu.isma.ui.common.out;

import com.goebl.simplify.PointExtractor;
import ru.nstu.isma.intg.api.IntgResultPoint;
import ru.nstu.isma.intg.api.calcmodel.DaeSystem;

class IntgResultPointExtractor implements PointExtractor<IntgResultPoint> {
    private static final int FACTOR = 1000000;

    private int yForDeIndex = 0;
    private int rhsForDeIndex = 0;
    private int rhsForAeIndex = 0;

    @Override
    public double getX(IntgResultPoint intgResultPoint) {
        return intgResultPoint.getX() * FACTOR;
    }

    @Override
    public double getY(IntgResultPoint intgResultPoint) {
        Double yForDe = tryGetYForDe(intgResultPoint);
        if (yForDe != null) return yForDe;

        Double rhsForDe = tryGetRhsForDe(intgResultPoint);
        if (rhsForDe != null) return rhsForDe;

        Double rhsForAe = tryGetRhsForAe(intgResultPoint);
        if (rhsForAe != null) return rhsForAe;

        yForDeIndex = 0;
        rhsForDeIndex = 0;
        rhsForAeIndex = 0;
        return getY(intgResultPoint);
    }

    private Double tryGetRhsForAe(IntgResultPoint intgResultPoint) {
        double[] rhsForAe = intgResultPoint.getRhs()[DaeSystem.RHS_AE_PART_IDX];
        if (rhsForAeIndex < rhsForAe.length) {
            double y = rhsForAe[rhsForAeIndex];
            rhsForAeIndex++;
            return y;
        }
        return null;
    }

    private Double tryGetRhsForDe(IntgResultPoint intgResultPoint) {
        double[] rhsForDe = intgResultPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX];
        if (rhsForDeIndex < rhsForDe.length) {
            double y = rhsForDe[rhsForDeIndex];
            rhsForDeIndex++;
            return y;
        }
        return null;
    }

    private Double tryGetYForDe(IntgResultPoint intgResultPoint) {
        if (yForDeIndex < intgResultPoint.getYForDe().length) {
            double y = intgResultPoint.getYForDe()[yForDeIndex];
            yForDeIndex++;
            return y;
        }
        return null;
    }
}
