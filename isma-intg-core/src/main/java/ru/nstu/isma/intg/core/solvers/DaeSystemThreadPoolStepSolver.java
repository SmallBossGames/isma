package ru.nstu.isma.intg.core.solvers;

import ru.nstu.isma.intg.api.calcmodel.DaeSystem;
import ru.nstu.isma.intg.api.calcmodel.DifferentialEquation;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.api.methods.IntgPoint;
import ru.nstu.isma.intg.api.methods.StageCalculator;
import ru.nstu.isma.intg.core.solvers.utils.DataBalanceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author Mariya Nasyrova
 * @since 25.02.16
 */
@Deprecated
public class DaeSystemThreadPoolStepSolver extends DefaultDaeSystemStepSolver {

    private final int threadCount;
    private final List<DataBalanceUtils.Range> threadTasks;
    private final ExecutorService executorService;

    public DaeSystemThreadPoolStepSolver(IntgMethod intgMethod, DaeSystem daeSystem, int threadCount, ExecutorService executorService) {
        super(intgMethod, daeSystem);

        this.threadCount = threadCount;
        this.executorService = executorService;

        threadTasks = DataBalanceUtils.getRankDataPortionRanges(daeSystem.getDifferentialEquationCount(), threadCount);
    }

    @Override
    protected double[] calculateRhsForDifferentialEquations(double[] yForDe, double[][] rhs) {
        DifferentialEquation[] diffEqs = getDaeSystem().getDifferentialEquations();

        return new ThreadPoolAction(threadCount, threadTasks, diffEqs.length, executorService) {
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

        return new ThreadPoolAction(threadCount, threadTasks, diffEqs.length, executorService) {
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

        return new ThreadPoolAction(threadCount, threadTasks, diffEqs.length, executorService) {
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

        return new ThreadPoolAction(threadCount, threadTasks, diffEqs.length, executorService) {
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

    private abstract static class ThreadPoolAction {

        private final int threadCount;
        private final List<DataBalanceUtils.Range> threadTasks;
        private final int resultSize;
        private final ExecutorService executorService;

        private volatile double[] result;

        public ThreadPoolAction(int threadCount, List<DataBalanceUtils.Range> threadTasks, int resultSize, ExecutorService executorService) {
            this.threadCount = threadCount;
            this.threadTasks = threadTasks;
            this.resultSize = resultSize;
            this.executorService = executorService;
        }

        abstract double calcValue(int deIdx);

        double[] make() {
            List<Callable<List<Result>>> calls = new ArrayList<>(threadCount);
            for (int i = 0; i < threadCount; i++) {
                final int finalI = i;
                calls.add(() -> {
                    DataBalanceUtils.Range eqRange = threadTasks.get(finalI);

                    List<Result> results = new ArrayList<>(eqRange.size());
                    for (int deIdx = eqRange.getStart(); deIdx < eqRange.getEnd(); deIdx++) {
                        double deValue = calcValue(deIdx);
                        results.add(new Result(deIdx, deValue));
                    }

                    return results;
                });
            }

            List<Future<List<Result>>> futures = null;
            try {
                futures = executorService.invokeAll(calls);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (futures == null) {
                throw new RuntimeException("aaa");
            }

            result = new double[resultSize];

            for (Future<List<Result>> future : futures) {
                List<Result> results = null;
                try {
                    results = future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

                if (results == null) {
                    throw new RuntimeException("bbb");
                }

                for (Result r : results) {
                    result[r.idx] = r.value;
                }
            }

            return result;
        }

        private static class Result {
            int idx;
            double value;

            Result(int idx, double value) {
                this.idx = idx;
                this.value = value;
            }
        }
    }

//    private ThreadPoolAction createCalculateRhsForDifferentialEquationsAction(double[] yForDe, double[][] rhs) {
//        DifferentialEquation[] diffEqs = getDaeSystem().getDifferentialEquations();
//
//        return new ThreadPoolAction(threadCount, threadTasks, diffEqs.length, executorService) {
//            @Override
//            double calcValue(int deIdx) {
//                return diffEqs[deIdx].apply(yForDe, rhs);
//            }
//        };
//    }
//
//    private ThreadPoolAction createNextYAction(IntgPoint fromPoint, double[][] stages){
//        DifferentialEquation[] diffEqs = getDaeSystem().getDifferentialEquations();
//
//        final double[][] deStages = {new double[0]};
//
//        return new ThreadPoolAction(threadCount, threadTasks, diffEqs.length, executorService) {
//            @Override
//            double calcValue(int deIdx) {
//                if (stages.length > 0)
//                    deStages[0] = stages[deIdx];
//
//                return getIntgMethod().nextY(
//                        fromPoint.getStep(),
//                        deStages[0],
//                        fromPoint.getY()[deIdx],
//                        fromPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX][deIdx]
//                );
//            }
//        };
//    }
//
//    private ThreadPoolAction createYkAction(StageCalculator stageCalc, IntgPoint fromPoint, double[][] stages) {
//        DifferentialEquation[] diffEqs = getDaeSystem().getDifferentialEquations();
//
//        return new ThreadPoolAction(threadCount, threadTasks, diffEqs.length, executorService) {
//            @Override
//            double calcValue(int deIdx) {
//                return stageCalc.yk(
//                        fromPoint.getStep(),
//                        fromPoint.getY()[deIdx],
//                        fromPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX][deIdx],
//                        stages[deIdx]
//                );
//            }
//        };
//    }
//
//    private ThreadPoolAction createKAction(StageCalculator stageCalc, IntgPoint fromPoint, double[][] stages, double[] yk, double[] fk) {
//        DifferentialEquation[] diffEqs = getDaeSystem().getDifferentialEquations();
//
//        return new ThreadPoolAction(threadCount, threadTasks, diffEqs.length, executorService) {
//            @Override
//            double calcValue(int deIdx) {
//                return stageCalc.k(
//                        fromPoint.getStep(),
//                        fromPoint.getY()[deIdx],
//                        fromPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX][deIdx],
//                        stages[deIdx],
//                        yk[deIdx],
//                        fk[deIdx]
//                );
//            }
//        };
//    }
}
