package ru.nstu.isma.intg.core.solvers;

import ru.nstu.isma.intg.api.calcmodel.DaeSystem;
import ru.nstu.isma.intg.api.calcmodel.DifferentialEquation;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.api.methods.IntgPoint;
import ru.nstu.isma.intg.api.methods.StageCalculator;
import ru.nstu.isma.intg.core.solvers.utils.DataBalanceUtils;

import java.util.List;

/**
 * @author Mariya Nasyrova
 * @since 25.02.16
 */
@Deprecated
public class DaeSystemThreadStepSolver extends DefaultDaeSystemStepSolver {

    private final int threadCount;
    private final List<DataBalanceUtils.Range> threadTasks;

    public DaeSystemThreadStepSolver(IntgMethod intgMethod, DaeSystem daeSystem) {
        super(intgMethod, daeSystem);

        threadCount = 2;
        threadTasks = DataBalanceUtils.getRankDataPortionRanges(daeSystem.getDifferentialEquationCount(), threadCount);
    }

    @Override
    protected double[] calculateRhsForDifferentialEquations(double[] yForDe, double[][] rhs) {
        DifferentialEquation[] diffEqs = getDaeSystem().getDifferentialEquations();

        return new ThreadAction(threadCount, threadTasks, diffEqs.length) {
            @Override
            double calcValue(int deIdx) {
                return diffEqs[deIdx].apply(yForDe, rhs);
            }
        }.make();

    }

    @Override
    protected double[] nextY(IntgPoint fromPoint, double[][] stages) {
        DifferentialEquation[] diffEqs = getDaeSystem().getDifferentialEquations();

        final double[][] deStages = {new double[0]};

        return new ThreadAction(threadCount, threadTasks, diffEqs.length) {
            @Override
            double calcValue(int deIdx) {
                if (stages.length > 0)
                    deStages[0] = stages[deIdx];

                return getIntgMethod().nextY(
                        fromPoint.getStep(),
                        deStages[0],
                        fromPoint.getY()[deIdx],
                        fromPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX][deIdx]
                );
            }
        }.make();
    }

    @Override
    protected double[] yk(StageCalculator stageCalc, IntgPoint fromPoint, double[][] stages) {
        DifferentialEquation[] diffEqs = getDaeSystem().getDifferentialEquations();

        return new ThreadAction(threadCount, threadTasks, diffEqs.length) {
            @Override
            double calcValue(int deIdx) {
                return stageCalc.yk(
                        fromPoint.getStep(),
                        fromPoint.getY()[deIdx],
                        fromPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX][deIdx],
                        stages[deIdx]
                );
            }
        }.make();
    }

    @Override
    protected double[] k(StageCalculator stageCalc, IntgPoint fromPoint, double[][] stages, double[] yk, double[] fk) {
        DifferentialEquation[] diffEqs = getDaeSystem().getDifferentialEquations();

        return new ThreadAction(threadCount, threadTasks, diffEqs.length) {
            @Override
            double calcValue(int deIdx) {
                return stageCalc.k(
                        fromPoint.getStep(),
                        fromPoint.getY()[deIdx],
                        fromPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX][deIdx],
                        stages[deIdx],
                        yk[deIdx],
                        fk[deIdx]
                );
            }
        }.make();
    }

    private abstract static class ThreadAction {

        private final int threadCount;
        private final List<DataBalanceUtils.Range> threadTasks;
        private final int resultSize;

        private volatile double[] result;

        public ThreadAction(int threadCount, List<DataBalanceUtils.Range> threadTasks, int resultSize) {
            this.threadCount = threadCount;
            this.threadTasks = threadTasks;
            this.resultSize = resultSize;
        }

        abstract double calcValue(int deIdx);

        double[] make() {

            result = new double[resultSize];

            Thread[] threads = new Thread[threadCount];

            for (int threadIdx = 0; threadIdx < threadCount; threadIdx++) {
                final int finalI = threadIdx;

                threads[threadIdx] = new Thread(() -> {
                    DataBalanceUtils.Range eqRange = threadTasks.get(finalI);

                    for (int deIdx = eqRange.getStart(); deIdx < eqRange.getEnd(); deIdx++) {
                        result[deIdx] = calcValue(deIdx);
                    }

                });
            }

            for (Thread thread : threads) {
                thread.start();
            }

            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return result;
        }

    }

}
