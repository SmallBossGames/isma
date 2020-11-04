package ru.nstu.isma.intg.tests;

import org.apache.ignite.Ignite;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.intg.api.IntgMetricData;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyProblem;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.core.solvers.DaeSystemThreadPool2StepSolver;
import ru.nstu.isma.intg.core.solvers.DefaultCauchyProblemSolver;
import ru.nstu.isma.intg.core.solvers.DefaultDaeSystemStepSolver;
import ru.nstu.isma.intg.demo.problems.ReactionDiffusionCauchyProblem;
import ru.nstu.isma.intg.lib.rungeKutta.rk22.Rk2IntgMethod;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Maria
 * @since 25.02.2016
 */
public class DaeSystemStepSolversPerformanceTest {

    private static CauchyProblem cauchyProblem;
    private static IntgMethod intgMethod;

    int threadCount = 2;
    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
//    int threadCount =  Runtime.getRuntime().availableProcessors();
//    ExecutorService executorService = Executors.newWorkStealingPool();

    private Ignite ignite;

    @BeforeClass
    public static void beforeTest() throws Exception {
        intgMethod = new Rk2IntgMethod();

        //cauchyProblem = new ReactionDiffusionCauchyProblem(1500, 1500);
        //cauchyProblem = new ReactionDiffusionCauchyProblem(100, 100);
        cauchyProblem = new ReactionDiffusionCauchyProblem(13, 13);
        cauchyProblem.getCauchyInitials().setStart(0.0);
        cauchyProblem.getCauchyInitials().setEnd(1.0);
        cauchyProblem.getCauchyInitials().setStepSize(0.0001);

        //ignite = IgniteClient.connect();
    }

    @After
    public void tearDown() throws Exception {
        //IgniteClient.disconnect(ignite);
    }

    @Test
    public void testDefaultDaeSystemStepSolver() throws Exception {
        new TestAction("testDefaultDaeSystemStepSolver", cauchyProblem) {
            @Override
            DefaultDaeSystemStepSolver createStepSolver() {
                return new DefaultDaeSystemStepSolver(intgMethod, cauchyProblem.getDaeSystem());
            }
        }.make();
    }

    /*@Test
    public void testDaeSystemThreadStepSolver() {
        new TestAction("testDaeSystemThreadStepSolver", cauchyProblem) {
            @Override
            DefaultDaeSystemStepSolver createStepSolver() {
                return new DaeSystemThreadStepSolver(intgMethod, cauchyProblem.getDaeSystem());
            }
        }.make();
    }*/

//    @Test
//    public void testDaeSystemThreadPoolStepSolver() throws InterruptedException {
////        int threadCount = 2;
////        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
//
//        new TestAction("testDaeSystemThreadPoolStepSolver", cauchyProblem) {
//            @Override
//            DefaultDaeSystemStepSolver createStepSolver() {
//                return new DaeSystemThreadPoolStepSolver(intgMethod, cauchyProblem.getDaeSystem(), threadCount, executorService);
//            }
//        }.make();
//
////        executorService.shutdown();
////        executorService.awaitTermination(10, TimeUnit.SECONDS);
//    }

    @Test
    public void testDaeSystemThreadPool2StepSolver() throws InterruptedException {
//        int threadCount = 2;
//        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
//        int threadCount =  Runtime.getRuntime().availableProcessors();
//        ExecutorService executorService = Executors.newWorkStealingPool();

        new TestAction("testDaeSystemThreadPool2StepSolver", cauchyProblem) {
            @Override
            DefaultDaeSystemStepSolver createStepSolver() {
                return new DaeSystemThreadPool2StepSolver(intgMethod, cauchyProblem.getDaeSystem(), threadCount, executorService);
            }
        }.make();

//        executorService.shutdown();
//        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }

    /*@Test
    public void testDaeSystemIgniteApplyStepSolver() throws Exception {
        new TestAction("testDaeSystemIgniteApplyStepSolver", cauchyProblem) {
            @Override
            DefaultDaeSystemStepSolver createStepSolver() {
                return new DaeSystemIgniteApplyStepSolver(intgMethod, cauchyProblem.getDaeSystem(), ignite);
            }
        }.make();
    }

    @Test
    public void testDaeSystemIgniteCallStepSolver() throws Exception {
        new TestAction("testDaeSystemIgniteCallStepSolver", cauchyProblem) {
            @Override
            DefaultDaeSystemStepSolver createStepSolver() {
                return new DaeSystemIgniteCallStepSolver(intgMethod, cauchyProblem.getDaeSystem(), ignite);
            }
        }.make();
    }*/

    private abstract static class TestAction {

        private Logger logger = LoggerFactory.getLogger(this.getClass());

        private final String name;
        private final CauchyProblem cauchyProblem;

        TestAction(String name, CauchyProblem cauchyProblem) {
            this.name = name;
            this.cauchyProblem = cauchyProblem;
        }

        abstract DefaultDaeSystemStepSolver createStepSolver();

        void make() {
            DefaultCauchyProblemSolver cauchyProblemSolver = new DefaultCauchyProblemSolver();
            DefaultDaeSystemStepSolver stepSolver = createStepSolver();

            IntgMetricData metricData = cauchyProblemSolver.solve(cauchyProblem, stepSolver, intgResultPoint -> {
            });

            logger.info(name + ": Calculated in {} ms. RHS calc count: {}",
                    metricData.getSimulationTime(), stepSolver.getRhsCalculationCount());

            //assertEquals(11, stepSolver.getStepCalculationCount());
            //assertEquals(34, stepSolver.getRhsCalculationCount());
        }

    }

}
