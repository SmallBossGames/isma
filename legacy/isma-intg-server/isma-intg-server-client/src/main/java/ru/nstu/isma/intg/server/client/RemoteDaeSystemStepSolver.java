package ru.nstu.isma.intg.server.client;

import com.google.common.base.Throwables;
import ru.nstu.isma.intg.api.calcmodel.DaeSystemChangeSet;
import ru.nstu.isma.intg.api.methods.IntegrationMethodRungeKutta;
import ru.nstu.isma.intg.api.methods.IntgPoint;
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver;

/**
 * @author Maria Nasyrova
 * @since 16.08.2015
 */
public class RemoteDaeSystemStepSolver implements DaeSystemStepSolver {

    private final ComputeEngineClient computeEngineClient;
    private final IntegrationMethodRungeKutta intgMethod;

    public RemoteDaeSystemStepSolver(IntegrationMethodRungeKutta intgMethod, ComputeEngineClient computeEngineClient) {
        this.computeEngineClient = computeEngineClient;
        this.intgMethod = intgMethod;
    }

    @Override
    public IntegrationMethodRungeKutta getIntgMethod() {
        return intgMethod;
    }

    @Override
    public double[][] calculateRhs(double[] yForDe) {
        try {
            return computeEngineClient.calculateRhs(yForDe);
        } catch (ComputeEngineClientException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public IntgPoint step(IntgPoint fromPoint) {
        try {
            return computeEngineClient.step(fromPoint);
        } catch (ComputeEngineClientException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void apply(DaeSystemChangeSet changeSet) {
        try {
            computeEngineClient.apply(changeSet);
        } catch (ComputeEngineClientException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public double[][] stages(IntgPoint fromPoint) {
        throw new UnsupportedOperationException("Should never be called on a client side.");
    }

    @Override
    public void dispose() {
        computeEngineClient.disconnect();
    }
}
