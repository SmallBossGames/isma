package ru.nstu.isma.intg.core.methods;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.intg.api.methods.AccuracyIntgController;
import ru.nstu.isma.intg.api.methods.IntgPoint;
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver;
import ru.nstu.isma.intg.core.methods.utils.MathUtils;

/**
 * @author Mariya Nasyrova
 * @since 14.08.14
 */
public abstract class BaseAccuracyIntgController extends AccuracyIntgController {

    private static final Logger logger = LoggerFactory.getLogger(BaseAccuracyIntgController.class);

    private static final double R = 1.0;

    @Override
    public AccuracyResults tune(IntgPoint fromPoint, double[][] stages, DaeSystemStepSolver stepSolver) {
        double step = fromPoint.getStep();
        double[] y = fromPoint.getY();

        double localError = getLocalError(stages, y);
        double actualAccuracy = getActualAccuracy(step, localError);
        double targetAccuracy = getAccuracy();
        boolean accurate = actualAccuracy <= targetAccuracy;

        if (logger.isDebugEnabled()) {
            logger.debug(
                    "Accuracy controller. Step size: {}, isAccurate: {}, localError: {}, actualAccuracy: {}, targetAccuracy: {} \nstages: {}",
                    step, accurate, localError, actualAccuracy, targetAccuracy,
                    ToStringBuilder.reflectionToString(stages));
        }

        if (accurate) {
            return new AccuracyResults(step, stages);
        }

        IntgPoint point = fromPoint.copyLight();
        double tunedStep = step;
        double[][] tunedStages = stages;

        long cycleCount = 0;
        while (!accurate) {
            if (cycleCount > 5) {
                logger.warn(
                        "Failed to make accurate. Accuracy cycle #{}. Initial step size: {}, Tuned step size: {}",
                        cycleCount, step, tunedStep);
                // Return original stages and step.
                return new AccuracyResults(step, stages);
            }

            tunedStep = getTunedStepSize(getQ(targetAccuracy, actualAccuracy), tunedStep);
            point.setStep(tunedStep);
            tunedStages = stepSolver.stages(point);

            localError = getLocalError(tunedStages, y);
            actualAccuracy = getActualAccuracy(tunedStep, localError);
            accurate = actualAccuracy <= targetAccuracy;

            if (logger.isDebugEnabled()) {
                logger.debug("Accuracy cycle #{}. Initial step size: {}, Tuned step size: {}, Is Accurate: {}",
                        cycleCount, step, tunedStep, accurate);
            }
            cycleCount++;
        }

        return new AccuracyResults(tunedStep, tunedStages);
    }

    private double getLocalError(double[][] stages, double[] y) {
        int deVarCount = stages.length;
        double[] norms = new double[deVarCount];
        double localErrorEstimation;

        for (int i = 0; i < deVarCount; i++) {
            localErrorEstimation = getLocalErrorEstimation(stages[i]);
            norms[i] = Math.abs(localErrorEstimation) / (Math.abs(y[i]) + R);
        }

        return MathUtils.max(norms);
    }

    private double getTunedStepSize(double q, double step) {
        return q * step;
    }

}
