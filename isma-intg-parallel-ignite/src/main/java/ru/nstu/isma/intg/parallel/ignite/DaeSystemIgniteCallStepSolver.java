package ru.nstu.isma.intg.parallel.ignite;

import org.apache.ignite.Ignite;
import ru.nstu.isma.intg.api.calcmodel.DaeSystem;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.api.methods.IntgPoint;
import ru.nstu.isma.intg.api.methods.StageCalculator;
import ru.nstu.isma.intg.core.solvers.DefaultDaeSystemStepSolver;

public class DaeSystemIgniteCallStepSolver extends DefaultDaeSystemStepSolver {

    private final Ignite ignite;

    public DaeSystemIgniteCallStepSolver(IntgMethod intgMethod, DaeSystem daeSystem, Ignite ignite) {
        super(intgMethod, daeSystem);
        this.ignite = ignite;
    }

    @Override
    protected double[] calculateRhsForDifferentialEquations(double[] yForDe, double[][] rhs) {
        return IgniteDaeSystemAction.createCalculateRhsForDifferentialEquationsAction(ignite, getDaeSystem(), yForDe, rhs)
                .call();
    }

    @Override
    protected double[] nextY(IntgPoint fromPoint, double[][] stages) {
        return IgniteDaeSystemAction.createNextYAction(ignite, getDaeSystem(), getIntgMethod(), fromPoint, stages)
                .call();
    }

    @Override
    protected double[] yk(StageCalculator stageCalc, IntgPoint fromPoint, double[][] stages) {
        return IgniteDaeSystemAction.createYkAction(ignite, getDaeSystem(), stageCalc, fromPoint, stages)
                .call();
    }

    @Override
    protected double[] k(StageCalculator stageCalc, IntgPoint fromPoint, double[][] stages, double[] yk, double[] fk) {
        return IgniteDaeSystemAction.createKAction(ignite, getDaeSystem(), stageCalc, fromPoint, stages, yk, fk)
                .call();
    }

}
