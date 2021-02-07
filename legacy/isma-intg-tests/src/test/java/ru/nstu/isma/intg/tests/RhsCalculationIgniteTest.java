package ru.nstu.isma.intg.tests;

import org.apache.ignite.Ignite;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.intg.api.IntgMetricData;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyProblem;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.core.solvers.DefaultCauchyProblemSolver;
import ru.nstu.isma.intg.demo.problems.ReactionDiffusionCauchyProblem;
import ru.nstu.isma.intg.lib.rungeKutta.rk22.Rk2IntgMethod;
import ru.nstu.isma.intg.parallel.ignite.DaeSystemIgniteApplyStepSolver;
import ru.nstu.isma.intg.parallel.ignite.IgniteClient;

import static org.junit.Assert.assertEquals;

public class RhsCalculationIgniteTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private CauchyProblem cauchyProblem;
    private IntgMethod intgMethod;

    @Before
    public void beforeTest() throws Exception {
        intgMethod = new Rk2IntgMethod();

        cauchyProblem = new ReactionDiffusionCauchyProblem(5, 5);
        cauchyProblem.getCauchyInitials().setStart(0.0);
        cauchyProblem.getCauchyInitials().setEnd(1.0);
        cauchyProblem.getCauchyInitials().setStepSize(0.1);
    }

    @Test
    public void testRhsCalculationCountWithoutControllers() throws Exception {
        Ignite ignite = IgniteClient.connect();

        DefaultCauchyProblemSolver cauchyProblemSolver = new DefaultCauchyProblemSolver();
        DaeSystemIgniteApplyStepSolver stepSolver = new DaeSystemIgniteApplyStepSolver(intgMethod, cauchyProblem.getDaeSystem(), ignite);

        IntgMetricData metricData = cauchyProblemSolver.solve(cauchyProblem, stepSolver, intgResultPoint -> {
        });

        IgniteClient.disconnect(ignite);

        logger.info("testRhsCalculationCountWithoutControllers: Calculated in {} ms. RHS calc count: {}",
                metricData.getSimulationTime(), stepSolver.getRhsCalculationCount());

        assertEquals(11, stepSolver.getStepCalculationCount());
        //assertEquals(34, stepSolver.getRhsCalculationCount());
    }

}
