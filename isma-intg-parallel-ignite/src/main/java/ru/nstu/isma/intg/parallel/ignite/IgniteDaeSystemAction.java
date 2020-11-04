package ru.nstu.isma.intg.parallel.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.lang.IgniteClosure;
import ru.nstu.isma.intg.api.calcmodel.DaeSystem;
import ru.nstu.isma.intg.api.calcmodel.DifferentialEquation;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.api.methods.IntgPoint;
import ru.nstu.isma.intg.api.methods.StageCalculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Maria
 * @since 25.02.2016
 */
abstract class IgniteDaeSystemAction {

    private final Ignite ignite;
    private final DaeSystem daeSystem;

    private double[] asyncResult;

    IgniteDaeSystemAction(Ignite ignite, DaeSystem daeSystem) {
        this.ignite = ignite;
        this.daeSystem = daeSystem;
    }

    abstract Double runFor(DifferentialEquation de);

    double[] apply() {
        Collection<Double> result = ignite.compute().apply(
                (IgniteClosure<DifferentialEquation, Double>) this::runFor,
                Arrays.asList(daeSystem.getDifferentialEquations())
        );
        return result.stream().mapToDouble(Double::doubleValue).toArray();
    }

    double[] applyAsync() {
        IgniteCompute asyncCompute = ignite.compute().withAsync();

        Collection<Double> result = asyncCompute.apply(
                (IgniteClosure<DifferentialEquation, Double>) this::runFor,
                Arrays.asList(daeSystem.getDifferentialEquations())
        );

        asyncCompute.future().listen(fut -> {
            asyncResult = result.stream().mapToDouble(Double::doubleValue).toArray();
        });

        return asyncResult;
    }

    double[] call() {
        Collection<IgniteCallable<Double>> calls = new ArrayList<>();

        for (DifferentialEquation de : daeSystem.getDifferentialEquations()) {
            calls.add((IgniteCallable<Double>) () -> runFor(de));
        }

        Collection<Double> result = ignite.compute().call(calls);

        return result.stream().mapToDouble(Double::doubleValue).toArray();
    }

    static IgniteDaeSystemAction createCalculateRhsForDifferentialEquationsAction(Ignite ignite, DaeSystem daeSystem,
                                                                                  double[] yForDe, double[][] rhs) {
        return new IgniteDaeSystemAction(ignite, daeSystem) {
            @Override
            public Double runFor(DifferentialEquation de) {
                return de.apply(yForDe, rhs);
            }
        };
    }

    static IgniteDaeSystemAction createNextYAction(Ignite ignite, DaeSystem daeSystem, IntgMethod intgMethod,
                                                   IntgPoint fromPoint, double[][] stages) {
        return new IgniteDaeSystemAction(ignite, daeSystem) {
            @Override
            public Double runFor(DifferentialEquation de) {
                int deIdx = de.getIndex();
                double[] deStages = stages.length == 0 ? new double[0] : stages[deIdx];
                return intgMethod.nextY(
                        fromPoint.getStep(),
                        deStages,
                        fromPoint.getY()[deIdx],
                        fromPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX][deIdx]
                );
            }
        };
    }

    static IgniteDaeSystemAction createYkAction(Ignite ignite, DaeSystem daeSystem, StageCalculator stageCalc,
                                                IntgPoint fromPoint, double[][] stages) {
        return new IgniteDaeSystemAction(ignite, daeSystem) {
            @Override
            public Double runFor(DifferentialEquation de) {
                int deIdx = de.getIndex();
                return stageCalc.yk(
                        fromPoint.getStep(),
                        fromPoint.getY()[deIdx],
                        fromPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX][deIdx],
                        stages[deIdx]
                );
            }
        };
    }

    static IgniteDaeSystemAction createKAction(Ignite ignite, DaeSystem daeSystem, StageCalculator stageCalc,
                                               IntgPoint fromPoint, double[][] stages, double[] yk, double[] fk) {
        return new IgniteDaeSystemAction(ignite, daeSystem) {
            @Override
            public Double runFor(DifferentialEquation de) {
                int deIdx = de.getIndex();
                return stageCalc.k(
                        fromPoint.getStep(),
                        fromPoint.getY()[deIdx],
                        fromPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX][deIdx],
                        stages[deIdx],
                        yk[deIdx],
                        fk[deIdx]
                );
            }
        };
    }

}
