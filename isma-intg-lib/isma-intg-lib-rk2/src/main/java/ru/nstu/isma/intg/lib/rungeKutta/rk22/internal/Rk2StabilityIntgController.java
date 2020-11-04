package ru.nstu.isma.intg.lib.rungeKutta.rk22.internal;

import ru.nstu.isma.intg.api.calcmodel.DaeSystem;
import ru.nstu.isma.intg.api.methods.IntgPoint;
import ru.nstu.isma.intg.core.methods.BaseStabilityIntgController;
import ru.nstu.isma.intg.core.methods.utils.MathUtils;

/**
 * @author Mariya Nasyrova
 * @since 29.08.14
 */
public class Rk2StabilityIntgController extends BaseStabilityIntgController {

    private static final double STABILITY_INTERVAL = 2.0;

    @Override public double getStabilityInterval() {
        return STABILITY_INTERVAL;
    }

    @Override public double getMaxJacobiEigenValue(IntgPoint point) {
        int varCount = point.getStages().length;
        double[] deltaKs = new double[varCount];
        double k1;
        double k2;

        for (int i = 0; i < varCount; i++) {
            k1 = point.getStages()[i][0];
            k2 = point.getStages()[i][1];

            if (k2 == k1) {
                deltaKs[i] = 0.0;
            } else {
                deltaKs[i] = Math.abs(point.getRhs()[DaeSystem.RHS_DE_PART_IDX][i] - k2) / Math.abs(k2 - k1);
            }
        }

        return  2.0 * MathUtils.max(deltaKs);
    }
}
