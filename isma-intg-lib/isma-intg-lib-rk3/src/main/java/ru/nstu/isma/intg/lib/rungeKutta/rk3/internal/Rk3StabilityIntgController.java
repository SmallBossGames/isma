package ru.nstu.isma.intg.lib.rungeKutta.rk3.internal;

import ru.nstu.isma.intg.api.methods.IntgPoint;
import ru.nstu.isma.intg.core.methods.BaseStabilityIntgController;
import ru.nstu.isma.intg.core.methods.utils.MathUtils;

/**
 * @author Mariya Nasyrova
 * @since 04.10.14
 */
public class Rk3StabilityIntgController extends BaseStabilityIntgController {

    private static final double STABILITY_INTERVAL = 2.5;

    @Override
    public double getStabilityInterval() {
        return STABILITY_INTERVAL;
    }

    @Override
    public double getMaxJacobiEigenValue(IntgPoint point) {
        // TODO: в y передаются алгебраические переменные, поэтому ориентируемся по размеру стадий, поскольку они созда.тся только для оду
        int varCount = point.getStages().length;
        double[] deltaKs = new double[varCount];

        double k1;
        double k2;
        double k3;

        for (int i = 0; i < varCount; i++) {
            k1 = point.getStages()[i][0];
            k2 = point.getStages()[i][1];
            k3 = point.getStages()[i][2];

            if (k2 == k1)
                deltaKs[i] = 0.0; // TODO: проверить корректность, может поэтому залипает?
            else
                deltaKs[i] = Math.abs(k1 - 2.0 * k2 + k3) / Math.abs(k2 - k1);
        }

        double maxDeltaK = MathUtils.max(deltaKs);
        return 0.5 * maxDeltaK;
    }
}
