package ru.nstu.isma.intg.server.client;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import org.github.jamm.MemoryMeter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.nstu.isma.intg.api.calcmodel.DaeSystem;
import ru.nstu.isma.intg.api.calcmodel.DifferentialEquation;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyInitials;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyProblem;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.api.methods.IntgPoint;
import ru.nstu.isma.intg.lib.IntgMethodLibrary;
import ru.nstu.isma.intg.lib.IntgMethodType;
import ru.nstu.isma.intg.server.models.*;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;

@RunWith(DataProviderRunner.class)
public class LargeOdeSystemIT {

    MemoryMeter memoryMeter = new MemoryMeter();
    private final ComputeEngineClient client = new ComputeEngineClient();

    @Before
    public void connectClient() throws ComputeEngineClientException {
        String serverHost = System.getProperty("TEST_COMPUTE_ENGINE_HOST", "localhost");
        client.connect(serverHost, 7890);
    }

    @After
    public void disconnectClient() throws ComputeEngineClientException {
        client.disconnect();
    }

    @Test
    public void calculateStructureSize() throws Exception {
        int size = 1000;
        CauchyProblem problem = createTestCauchyProblem(size);
        IntgMethod method = IntgMethodLibrary.createMethod(IntgMethodType.RUNGE_KUTTA_MERSON);

        client.loadIntgMethod(method);
        client.loadDaeSystem(problem.getDaeSystem());

        double step = problem.getCauchyInitials().getStepSize();
        double[] yForDe = problem.getCauchyInitials().getY0();
        double[][] rhs = client.calculateRhs(yForDe);
        IntgPoint initialPoint = new IntgPoint(step, yForDe, rhs);

        System.out.println(String.format(
                "Equation count: %d", size));
        System.out.println(String.format(
                "Object of one Ode: %d", memoryMeter.measureDeep(problem.getDaeSystem().getDifferentialEquations()[0])));
        System.out.println(String.format(
                "Object size of CauchyProblem: %d", memoryMeter.measureDeep(problem)));
        System.out.println(String.format(
                "Object size of OdeSystem: %d", memoryMeter.measureDeep(problem.getDaeSystem())));
        System.out.println(String.format(
                "Object size of Runge-Kutta method: %d", memoryMeter.measureDeep(method)));
        System.out.println(String.format(
                "Object size of an initial IntgPoint: %d", memoryMeter.measureDeep(initialPoint)));

        StepRequest intgPointRequest = new StepRequest(BinaryObject.serialize(initialPoint));
        LoadIntgMethodRequest intgMethodRequest = new LoadIntgMethodRequest(BinaryObject.serialize(method));
        LoadDaeSystemRequest odeSystemRequest = new LoadDaeSystemRequest(BinaryObject.serialize(problem.getDaeSystem()));

        System.out.println(String.format(
                "Object size of a serialized IntgPoint request: %d", memoryMeter.measureDeep(intgPointRequest)));
        System.out.println(String.format(
                "Object size of a serialized IntgMethod request: %d", memoryMeter.measureDeep(intgMethodRequest)));
        System.out.println(String.format(
                "Object size of a serialized OdeSystem request: %d", memoryMeter.measureDeep(odeSystemRequest)));

        IntgPoint resultPoint = client.step(initialPoint);
        assertNotNull(resultPoint);

        System.out.println(String.format(
                "Object size of a result IntgPoint: %d", memoryMeter.measureDeep(resultPoint)));
        System.out.println(String.format(
                "Object size of a serialized IntgPointResponse: %d",
                memoryMeter.measureDeep(StepResponse.create(resultPoint))));
        System.out.println();
    }

    @Test
    @DataProvider({ "100", "1000", "10000", "200000" })
    public void testOdeSystemSize(int size) throws Exception {
        CauchyProblem problem = createTestCauchyProblem(size);

        IntgMethod method = IntgMethodLibrary.createMethod(IntgMethodType.EULER);
        client.loadIntgMethod(method);
        client.loadDaeSystem(problem.getDaeSystem());

        assertNotNull(integrateInitialStep(problem));
    }

    private IntgPoint integrateInitialStep(CauchyProblem problem)
            throws ComputeEngineClientException {
        double step = problem.getCauchyInitials().getStepSize();
        double[] yForDe = problem.getCauchyInitials().getY0();

        double[][] rhs = client.calculateRhs(yForDe);
        IntgPoint fromPoint = new IntgPoint(step, yForDe, rhs);

        return client.step(fromPoint);
    }

    private CauchyProblem createTestCauchyProblem(int size) {
        double[] y0 = new double[size];
        Arrays.fill(y0, 0.0d);
        CauchyInitials cauchyInitials = new CauchyInitials();
        cauchyInitials.setInterval(0, 50);
        cauchyInitials.setStepSize(0.5);
        cauchyInitials.setY0(y0);

        DifferentialEquation[] des = new DifferentialEquation[size];
        for (int i = 0; i < size; i++) {
            final int index = i;
            des[i] = new DifferentialEquation(i, (y, rhs) -> y[index], String.format("y[%d]", index));
        }
        DaeSystem odeSystem = new DaeSystem(des);

        CauchyProblem problem = new CauchyProblem();
        problem.setCauchyInitials(cauchyInitials);
        problem.setDaeSystem(odeSystem);
        return problem;
    }

}
