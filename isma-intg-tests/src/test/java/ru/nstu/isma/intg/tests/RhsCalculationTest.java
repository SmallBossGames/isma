package ru.nstu.isma.intg.tests;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.intg.api.IntgMetricData;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyProblem;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.core.solvers.DefaultCauchyProblemSolver;
import ru.nstu.isma.intg.core.solvers.DefaultDaeSystemStepSolver;
import ru.nstu.isma.intg.demo.problems.ReactionDiffusionCauchyProblem;
import ru.nstu.isma.intg.lib.rungeKutta.rk22.Rk2IntgMethod;

import static org.junit.Assert.assertEquals;

public class RhsCalculationTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private CauchyProblem cauchyProblem;
    private IntgMethod intgMethod;

    @Before
    public void beforeTest() throws Exception {
        intgMethod = new Rk2IntgMethod();

//        cauchyProblem = new ReactionDiffusionCauchyProblem(5, 5);
//        cauchyProblem.getCauchyInitials().setStart(0.0);
//        cauchyProblem.getCauchyInitials().setEnd(10.0);
//        cauchyProblem.getCauchyInitials().setStepSize(0.001);

        cauchyProblem = new ReactionDiffusionCauchyProblem(2, 2);
        cauchyProblem.getCauchyInitials().setStart(0.0);
        cauchyProblem.getCauchyInitials().setEnd(1.0);
        cauchyProblem.getCauchyInitials().setStepSize(0.1);
    }

    @Test
    public void testRhsCalculationCountWithoutControllers() throws Exception {
        DefaultCauchyProblemSolver cauchyProblemSolver = new DefaultCauchyProblemSolver();
        DefaultDaeSystemStepSolver stepSolver = new DefaultDaeSystemStepSolver(intgMethod, cauchyProblem.getDaeSystem());

        IntgMetricData metricData = cauchyProblemSolver.solve(cauchyProblem, stepSolver, intgResultPoint -> {
        });

        logger.info("testRhsCalculationCountWithoutControllers: Calculated in {} ms. RHS calc count: {}",
                metricData.getSimulationTime(), stepSolver.getRhsCalculationCount());

        assertEquals(10001, stepSolver.getStepCalculationCount());
        assertEquals(20003, stepSolver.getRhsCalculationCount());
    }

    @Test
    public void testRhsCalculationCountWithAccuracy() throws Exception {
        intgMethod.getAccuracyController().setAccuracy(0.00001);
        intgMethod.getAccuracyController().setEnabled(true);

        DefaultCauchyProblemSolver cauchyProblemSolver = new DefaultCauchyProblemSolver();
        DefaultDaeSystemStepSolver stepSolver = new DefaultDaeSystemStepSolver(intgMethod, cauchyProblem.getDaeSystem());

        IntgMetricData metricData = cauchyProblemSolver.solve(cauchyProblem, stepSolver, intgResultPoint -> {
        });

        logger.info("testRhsCalculationCountWithAccuracy: Calculated in {} ms. RHS calc count: {}",
                metricData.getSimulationTime(), stepSolver.getRhsCalculationCount());

        assertEquals(10039, stepSolver.getStepCalculationCount());
        assertEquals(20160, stepSolver.getRhsCalculationCount());
    }

    @Test
    public void testRhsCalculationCountWithAccuracyAndStability() throws Exception {
        intgMethod.getAccuracyController().setAccuracy(0.00001);
        intgMethod.getAccuracyController().setEnabled(true);
        intgMethod.getStabilityController().setEnabled(true);

        DefaultCauchyProblemSolver cauchyProblemSolver = new DefaultCauchyProblemSolver();
        DefaultDaeSystemStepSolver stepSolver = new DefaultDaeSystemStepSolver(intgMethod, cauchyProblem.getDaeSystem());

        IntgMetricData metricData = cauchyProblemSolver.solve(cauchyProblem, stepSolver, intgResultPoint -> {
        });

        logger.info("testRhsCalculationCountWithAccuracyAndStability: Calculated in {} ms. RHS calc count: {}",
                metricData.getSimulationTime(), stepSolver.getRhsCalculationCount());

        assertEquals(10039, stepSolver.getStepCalculationCount());
        assertEquals(20160, stepSolver.getRhsCalculationCount());
    }

}
