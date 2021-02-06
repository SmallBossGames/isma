package ru.nstu.isma.intg.demo.app.services;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.intg.api.IntgMetricData;
import ru.nstu.isma.intg.api.IntgResultPoint;
import ru.nstu.isma.intg.api.calcmodel.DaeSystem;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyProblem;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver;
import ru.nstu.isma.intg.core.solvers.DefaultCauchyProblemSolver;
import ru.nstu.isma.intg.core.solvers.DefaultDaeSystemStepSolver;
import ru.nstu.isma.intg.demo.app.models.MethodModel;
import ru.nstu.isma.intg.demo.app.models.ProblemModel;
import ru.nstu.isma.intg.server.client.ComputeEngineClient;
import ru.nstu.isma.intg.server.client.ComputeEngineClientException;
import ru.nstu.isma.intg.server.client.RemoteDaeSystemStepSolver;

import java.util.function.Consumer;

/**
 * @author Mariya Nasyrova
 * @since 17.10.2014
 */
public final class SimulationService {

    private static ComputeEngineClient computeEngineClient = new ComputeEngineClient();

    private final static Logger logger = LoggerFactory.getLogger(SimulationService.class);

    private SimulationService() {
    }

    public static IntgMetricData solve(ProblemModel problem, MethodModel method, Consumer<IntgResultPoint> resultConsumer) {
        CauchyProblem cauchyProblem = CauchyProblemService.getCauchyProblem(problem);
        IntgMethod intgMethod = IntgMethodService.getIntgMethod(method);

        DefaultCauchyProblemSolver cauchyProblemSolver = new DefaultCauchyProblemSolver();

        DaeSystemStepSolver stepSolver;
        try {
            if (method.isParallel()) {
                stepSolver = createParallelOdeSystemStepSolver(
                        method.getIntgServerHost(), method.getIntgServerPort(), intgMethod,
                        cauchyProblem.getDaeSystem());
            } else {
                stepSolver = new DefaultDaeSystemStepSolver(intgMethod, cauchyProblem.getDaeSystem());
            }

            IntgMetricData metricData = cauchyProblemSolver.solve(cauchyProblem, stepSolver, resultConsumer);
            logCalculationStatistic(metricData, stepSolver);
            return metricData;
        } finally {
            if (method.isParallel() && computeEngineClient != null) {
                computeEngineClient.disconnect();
            }
        }
    }

    private static DaeSystemStepSolver createParallelOdeSystemStepSolver(
            String serverHost, int serverPort, IntgMethod intgMethod, DaeSystem daeSystem) {
        try {
            computeEngineClient.connect(serverHost, serverPort);
            computeEngineClient.loadIntgMethod(intgMethod);
            computeEngineClient.loadDaeSystem(daeSystem);
            return new RemoteDaeSystemStepSolver(intgMethod, computeEngineClient);
        } catch (ComputeEngineClientException e) {
            throw Throwables.propagate(e);
        }
    }

    private static void logCalculationStatistic(IntgMetricData metricData, DaeSystemStepSolver stepSolver) {
        if (logger.isInfoEnabled()) {
            long stepCalculationCount = stepSolver instanceof DefaultDaeSystemStepSolver ?
                    ((DefaultDaeSystemStepSolver) stepSolver).getStepCalculationCount() : -1;
            long rhsCalculationCount = stepSolver instanceof DefaultDaeSystemStepSolver ?
                    ((DefaultDaeSystemStepSolver) stepSolver).getRhsCalculationCount() : -1;
            logger.info("Simulation time: {} ms; Step calculation count: {}; RHS calculation count: {}",
                    metricData.getSimulationTime(), stepCalculationCount, rhsCalculationCount);
        }
    }

}