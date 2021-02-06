package ru.nstu.isma.intg.core.methods;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.intg.api.methods.IntgPoint;
import ru.nstu.isma.intg.api.methods.StabilityIntgController;

/**
 * @author Mariya Nasyrova
 * @since 29.08.14
 */
public abstract class BaseStabilityIntgController extends StabilityIntgController {

    private static final Logger logger = LoggerFactory.getLogger(BaseStabilityIntgController.class);

    public double predictNextStepSize(IntgPoint toPoint) {
        double nextStepSize = toPoint.getNextStep();

        double maxJacobiEigenValue = getMaxJacobiEigenValue(toPoint);
        boolean isStable = maxJacobiEigenValue <= getStabilityInterval();

        if (logger.isDebugEnabled()) {
            logger.debug(
                    "Stability controller. Step: {}, isStable: {}, maxJacobiEigenValue: {}, stabilityInterval: {} \npoint: {}",
                    toPoint.getStep(), isStable, maxJacobiEigenValue, getStabilityInterval(),
                    ToStringBuilder.reflectionToString(toPoint));
        }

        if (!isStable) {
            nextStepSize = getTunedStepSize(maxJacobiEigenValue, toPoint.getNextStep());
            if (logger.isDebugEnabled()) {
                logger.debug("Stability controller predicted a next step size: {}", nextStepSize);
            }
        }

        return nextStepSize;
    }

    private double getTunedStepSize(double maxJacobiEigenValue, double step) {
        return getStabilityInterval() * step / maxJacobiEigenValue;
    }

}
