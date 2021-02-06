package ru.nstu.isma.core.sim.controller;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.nstu.isma.intg.api.IntgResultMemoryStore;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver;
import ru.nstu.isma.intg.core.methods.EventDetectionIntgController;
import ru.nstu.isma.intg.core.solvers.DefaultDaeSystemStepSolver;
import ru.nstu.isma.intg.lib.euler.EulerIntgMethod;

import static org.junit.Assert.assertArrayEquals;

@RunWith(DataProviderRunner.class)
public class HybridSystemSimulatorTest {

    private static final double COMPARISON_DELTA = 0.000001;
    private HybridSystemSimulator simulator;
    private IntgMethod method;

    @Before
    public void beforeTest() {
        simulator = new HybridSystemSimulator();
        method = new EulerIntgMethod();
    }

    @DataProvider
    public static Object[][] transitionTests() {
        return new Object[][]{
                {"" +
                        "t' = 1; y' = 0;" +
                        "state s1 (t == 0) { set y = 1; } from init;" +
                        "state s2 (t == 1) { set y = 2; } from s1;" +
                        "state s3 (t == 2) { set y = 3; } from s2;",
                        new double[]{1.0, 2.0, 3.0, 3.0}
                },
                {"" +
                        "t' = 1; y' = 0;" +
                        "state s1 (t <= 2 || y < 2) { y' = 1; } from init, s2;" +
                        "state s2 (t > 2 && y >= 2) { y' = -1; } from s1;",
                        new double[]{0.0, 1.0, 2.0, 3.0, 2.0, 1.0, 2.0}
                }
        };
    }

    @Test
    @UseDataProvider("transitionTests")
    public void testStateTransitions(String model, double[] expectedY) throws Exception {
        TestUtils.CompilationResult compilationResult = TestUtils.compileModel("simpleTransitions", model);
        SimulationInitials initials = new SimulationInitials(
                TestUtils.newOdeInitials(compilationResult), 1, 0, expectedY.length);

        DaeSystemStepSolver stepSolver = new DefaultDaeSystemStepSolver(
                method, compilationResult.getHybridSystem().getDaeSystem());

        EventDetectionIntgController eventDetector = new EventDetectionIntgController(0.8);
        eventDetector.setEnabled(false);
        double eventDetectionStepBoundLow = Double.MIN_VALUE;

        IntgResultMemoryStore memoryStore = new IntgResultMemoryStore();
        simulator.run(compilationResult.getHybridSystem(), stepSolver, initials, eventDetector, eventDetectionStepBoundLow, memoryStore);

        int yIndex = compilationResult.getIndexProvider().getDifferentialEquationIndex("y");
        memoryStore.read(intgResultPoints -> {
            double[] y = intgResultPoints.stream().mapToDouble(point -> point.getYForDe()[yIndex]).toArray();
            System.out.println(ReflectionToStringBuilder.reflectionToString(y));
            assertArrayEquals(expectedY, y, COMPARISON_DELTA);
        });
    }

}