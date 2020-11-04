package ru.nstu.isma.intg.core.solvers;

import ru.nstu.isma.intg.api.calcmodel.DaeSystem;
import ru.nstu.isma.intg.api.calcmodel.DifferentialEquation;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.api.methods.IntgPoint;
import ru.nstu.isma.intg.api.methods.StageCalculator;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * @author Mariya Nasyrova
 * @since 25.02.16
 */
public class DaeSystemThreadPool2StepSolver extends DefaultDaeSystemStepSolver {

    private final ExecutorService executorService;
    private final int threadCount;

    public DaeSystemThreadPool2StepSolver(IntgMethod intgMethod, DaeSystem daeSystem, int threadCount, ExecutorService executorService) {
        super(intgMethod, daeSystem);

        this.threadCount = threadCount;
        this.executorService = executorService;
    }

    @Override
    protected double[] calculateRhsForDifferentialEquations(double[] yForDe, double[][] rhs) {
        DifferentialEquation[] diffEqs = getDaeSystem().getDifferentialEquations();

//        return new ThreadPoolAction(diffEqs.length, executorService) {
//            @Override
//            double calcValue(int deIdx) {
//                return diffEqs[deIdx].apply(yForDe, rhs);
//            }
//        }.make();

        return execute(diffEqs.length, (deIdx) -> {
            return diffEqs[deIdx].apply(yForDe, rhs);
        });
    }

    @Override
    protected double[] nextY(IntgPoint fromPoint, double[][] stages) {
        DifferentialEquation[] diffEqs = getDaeSystem().getDifferentialEquations();

        final double[][] deStages = {new double[0]};

//        return new ThreadPoolAction(diffEqs.length, executorService) {
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
//        }.make();

        return execute(diffEqs.length, (deIdx) -> {
            if (stages.length > 0)
                deStages[0] = stages[deIdx];

            return getIntgMethod().nextY(
                    fromPoint.getStep(),
                    deStages[0],
                    fromPoint.getY()[deIdx],
                    fromPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX][deIdx]
            );
        });
    }

    @Override
    protected double[] yk(StageCalculator stageCalc, IntgPoint fromPoint, double[][] stages) {
        DifferentialEquation[] diffEqs = getDaeSystem().getDifferentialEquations();

//        return new ThreadPoolAction(diffEqs.length, executorService) {
//            @Override
//            double calcValue(int deIdx) {
//                return stageCalc.yk(
//                        fromPoint.getStep(),
//                        fromPoint.getY()[deIdx],
//                        fromPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX][deIdx],
//                        stages[deIdx]
//                );
//            }
//        }.make();

        return execute(diffEqs.length, (deIdx) -> {
            return stageCalc.yk(
                    fromPoint.getStep(),
                    fromPoint.getY()[deIdx],
                    fromPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX][deIdx],
                    stages[deIdx]
            );
        });
    }

    @Override
    protected double[] k(StageCalculator stageCalc, IntgPoint fromPoint, double[][] stages, double[] yk, double[] fk) {
        DifferentialEquation[] diffEqs = getDaeSystem().getDifferentialEquations();

//        return new ThreadPoolAction(diffEqs.length, executorService) {
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
//        }.make();

        return execute(diffEqs.length, (deIdx) -> {
            return stageCalc.k(
                    fromPoint.getStep(),
                    fromPoint.getY()[deIdx],
                    fromPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX][deIdx],
                    stages[deIdx],
                    yk[deIdx],
                    fk[deIdx]
            );
        });
    }

    @FunctionalInterface
    public interface CalcFunction {
        double apply(int index);
    }

    private double[] execute(int resultSize, CalcFunction calcFunction) {
        double[] result = new double[resultSize];

        final CountDownLatch latch = new CountDownLatch(threadCount);
        int width = resultSize / threadCount;
        for (int i = 0; i < threadCount; i++) {
            int offset = i * width;
            executorService.execute(() -> {
                for (int j = 0; j < width; j++) {
                    final int index = offset + j;
                    result[index] = calcFunction.apply(index);
                }
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        List<Callable<Object>> calls = new ArrayList<>(threadCount);
//        int width = resultSize / threadCount;
//        for (int i = 0; i < threadCount; i++) {
//            int offset = i * width;
//            calls.add(() -> {
//                for (int j = 0; j < width; j++) {
//                    final int index = offset + j;
//                    result[index] = calcValue.apply(index);
//                }
//                return null;
//            });
//        }
////        try {
//        List<Future<Object>> futures = new ArrayList<>(threadCount);
//            for (int i = 0; i < calls.size(); i++) {
//                futures.add(executorService.submit(calls.get(i)));
//            }
////        for (int i = 0; i < futures.size(); i++) {
////            try {
////                futures.get(i).get();
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            } catch (ExecutionException e) {
////                e.printStackTrace();
////            }
////        }
//
//        boolean is1Done;
//        boolean is2Done;
//        for(;;) {
//            is1Done = futures.get(0).isDone();
//            is2Done = futures.get(1).isDone();
//            if (is1Done && is2Done) {
//                break;
//            }
//        }
//
////            executorService.invokeAll(calls);
////        } catch (InterruptedException e) {
////            throw new RuntimeException(e);
////        }
        return result;
    }

//    private abstract static class ThreadPoolAction {
//
//        private final int resultSize;
//        private final ExecutorService executorService;
//
//        private volatile double[] result;
//
//        public ThreadPoolAction(int resultSize, ExecutorService executorService) {
//            this.resultSize = resultSize;
//            this.executorService = executorService;
//        }
//
//        abstract double calcValue(int deIdx);
//
//        double[] make() {
//            result = new double[resultSize];
//
//            final CountDownLatch latch = new CountDownLatch(threadCount);
//            int width = resultSize / threadCount;
//            for (int i = 0; i < threadCount; i++) {
//                int offset = i * width;
//                executorService.execute(() -> {
//                    for (int j = 0; j < width; j++) {
//                        final int index = offset + j;
//                        result[index] = calcValue(index);
//                    }
//                    latch.countDown();
//                });
//            }
//
//            try {
//                latch.await(1, TimeUnit.DAYS);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return result;
//        }
//
//    }

}
