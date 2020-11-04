package ru.nstu.isma.intg.api.methods;

/**
 * @author Maria Nasyrova
 * @since 25.02.2015
 */
public abstract class StabilityIntgController extends IntgController {

    public abstract double predictNextStepSize(IntgPoint toPoint);

    protected abstract double getStabilityInterval();

    protected abstract double getMaxJacobiEigenValue(IntgPoint point);

}
