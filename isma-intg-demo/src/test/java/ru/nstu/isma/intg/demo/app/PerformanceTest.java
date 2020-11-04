package ru.nstu.isma.intg.demo.app;

import org.junit.Ignore;
import org.junit.Test;
import ru.nstu.isma.intg.api.IntgMetricData;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyProblem;
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver;
import ru.nstu.isma.intg.core.solvers.DefaultCauchyProblemSolver;
import ru.nstu.isma.intg.core.solvers.DefaultDaeSystemStepSolver;
import ru.nstu.isma.intg.demo.problems.ReactionDiffusionCauchyProblem;
import ru.nstu.isma.intg.lib.rungeKutta.rk22.Rk2IntgMethod;

import static org.junit.Assert.assertNotNull;

@Ignore
public class PerformanceTest {

    /*
        Старый солвер посчитал 317*317*2, т.е. 200 978, а при 318*318 сдулся.
        Новый солвер посчитал 318*318*2, т.е. 202 248, а 319 уже не взял.
     */

    @Test
    public void testReactionDiffusionWithRungeKutta22Method() throws Exception {
        //CauchyProblem problem = new ReactionDiffusionCauchyProblem(319, 319);
        //CauchyProblem problem = new ReactionDiffusionCauchyProblem(2, 2);
        CauchyProblem problem = new ReactionDiffusionCauchyProblem(16, 16);
        Rk2IntgMethod method = new Rk2IntgMethod();
        method.getAccuracyController().setEnabled(true);
        method.getAccuracyController().setAccuracy(0.01);
        method.getStabilityController().setEnabled(true);
        DaeSystemStepSolver stepSolver = new DefaultDaeSystemStepSolver(method, problem.getDaeSystem());
        DefaultCauchyProblemSolver cauchyProblemSolver = new DefaultCauchyProblemSolver();

        IntgMetricData metricData = cauchyProblemSolver.solve(problem, stepSolver, intgResultPoint -> {
        });
        assertNotNull(metricData);
        System.out.println("Total calculation time: " + Long.toString(metricData.getSimulationTime()));
    }

}
