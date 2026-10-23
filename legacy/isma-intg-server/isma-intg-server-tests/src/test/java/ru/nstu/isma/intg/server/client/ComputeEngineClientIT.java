package ru.nstu.isma.intg.server.client;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyProblem;
import ru.nstu.isma.intg.api.methods.IIntegrationMethodFactory;
import ru.nstu.isma.intg.api.methods.IntgPoint;
import ru.nstu.isma.intg.demo.problems.FourDimensionalCauchyProblem;
import ru.nstu.isma.intg.demo.problems.ReactionDiffusionCauchyProblem;
import ru.nstu.isma.intg.demo.problems.TwoDimensionalCauchyProblem;

import static org.junit.Assert.assertNotNull;

public class ComputeEngineClientIT {
	private final IIntegrationMethodFactory eulerFactory = new ru.nstu.isma.intg.lib.euler.IntegrationMethodFactory();
	private final IIntegrationMethodFactory rk22Factory = new ru.nstu.isma.intg.lib.rungeKutta.rk22.IntegrationMethodFactory();
	private final IIntegrationMethodFactory rk3Factory = new ru.nstu.isma.intg.lib.rungeKutta.rk3.IntegrationMethodFactory();
	private final IIntegrationMethodFactory rkmFactory = new ru.nstu.isma.intg.lib.rungeKutta.rkMerson.IntegrationMethodFactory();

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
	public void test2DCauchyProblemWithEulerMethod() throws Exception {
		var method = eulerFactory.create();
		CauchyProblem problem = new TwoDimensionalCauchyProblem();

        client.loadIntgMethod(method);
        client.loadDaeSystem(problem.getDaeSystem());

        assertNotNull(integrateInitialStep(problem));
	}

	@Test
	public void test2DCauchyProblemWithRungeKutta22Method() throws Exception {
		var method = rk22Factory.create();
		method.getAccuracyController().setAccuracy(0.01);

		CauchyProblem problem = new TwoDimensionalCauchyProblem();
        client.loadDaeSystem(problem.getDaeSystem());

        method.getAccuracyController().setEnabled(false);
        method.getStabilityController().setEnabled(false);
        client.loadIntgMethod(method);
		assertNotNull(integrateInitialStep(problem));

		method.getAccuracyController().setEnabled(true);
		method.getStabilityController().setEnabled(false);
        client.loadIntgMethod(method);
		assertNotNull(integrateInitialStep(problem));

		method.getAccuracyController().setEnabled(true);
		method.getStabilityController().setEnabled(true);
        client.loadIntgMethod(method);
        assertNotNull(integrateInitialStep(problem));
	}

	@Test
	public void test2DCauchyProblemWithRungeKutta3Method() throws Exception {
		var method = rk3Factory.create();
		method.getAccuracyController().setAccuracy(0.01);

		CauchyProblem problem = new TwoDimensionalCauchyProblem();
        client.loadDaeSystem(problem.getDaeSystem());

		method.getAccuracyController().setEnabled(false);
		method.getStabilityController().setEnabled(false);
        client.loadIntgMethod(method);
        assertNotNull(integrateInitialStep(problem));

		method.getAccuracyController().setEnabled(true);
		method.getStabilityController().setEnabled(false);
        client.loadIntgMethod(method);
        assertNotNull(integrateInitialStep(problem));

		method.getAccuracyController().setEnabled(true);
		method.getStabilityController().setEnabled(true);
        client.loadIntgMethod(method);
        assertNotNull(integrateInitialStep(problem));
	}

	@Test
	public void test2DCauchyProblemWithRkMersonMethod() throws Exception {
		var method = rkmFactory.create();
		method.getAccuracyController().setAccuracy(0.01);

		CauchyProblem problem = new TwoDimensionalCauchyProblem();
        client.loadDaeSystem(problem.getDaeSystem());

		method.getAccuracyController().setEnabled(false);
        client.loadIntgMethod(method);
        assertNotNull(integrateInitialStep(problem));

		method.getAccuracyController().setEnabled(true);
        client.loadIntgMethod(method);
        assertNotNull(integrateInitialStep(problem));
	}

	@Test
	public void test4DCauchyProblemWithEulerMethod() throws Exception {
		var method = eulerFactory.create();
		CauchyProblem problem = new FourDimensionalCauchyProblem();

        client.loadDaeSystem(problem.getDaeSystem());
        client.loadIntgMethod(method);

		assertNotNull(integrateInitialStep(problem));
	}

	@Test
	public void testReactionDiffusionCauchyProblemWithEulerMethod() throws Exception {
		var method = eulerFactory.create();
		CauchyProblem problem = new ReactionDiffusionCauchyProblem(2, 2);

        client.loadDaeSystem(problem.getDaeSystem());
        client.loadIntgMethod(method);

		assertNotNull(integrateInitialStep(problem));
	}

	private IntgPoint integrateInitialStep(CauchyProblem problem)
			throws ComputeEngineClientException {

		double step = problem.getCauchyInitials().getStepSize();
		double[] yForDe = problem.getCauchyInitials().getY0();

        double[][] rhs = client.calculateRhs(yForDe);
        IntgPoint fromPoint = new IntgPoint(step, yForDe, rhs, new double[0][], step);

		return client.step(fromPoint);
	}

}
