package ru.nstu.isma.core.sim.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.core.hsm.HSM;
import ru.nstu.isma.core.hsm.var.HMDerivativeEquation;
import ru.nstu.isma.core.sim.controller.gen.AnalyzedHybridSystemClassBuilder;
import ru.nstu.isma.core.sim.controller.gen.EquationIndexProvider;
import ru.nstu.isma.core.sim.controller.gen.SourceCodeCompiler;
import ru.nstu.isma.intg.api.IntgMetricData;
import ru.nstu.isma.intg.api.IntgResultMemoryStore;
import ru.nstu.isma.intg.api.IntgResultPointFileReader;
import ru.nstu.isma.intg.api.IntgResultPointFileWriter;
import ru.nstu.isma.intg.api.calcmodel.HybridSystem;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyInitials;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver;
import ru.nstu.isma.intg.core.methods.EventDetectionIntgController;
import ru.nstu.isma.intg.core.solvers.DefaultDaeSystemStepSolver;
import ru.nstu.isma.intg.server.client.ComputeEngineClient;
import ru.nstu.isma.intg.server.client.RemoteDaeSystemStepSolver;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Bessonov Alex
 * on 04.01.2015.
 */
public class Controller {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String DEFAULT_PACKAGE_NAME = "ru.nstu.isma.core.simulation.controller";
    private static final String DEFAULT_CLASS_NAME = "AnalyzedHybridSystem";

    private HSM hsm;

    private IntgMethod method;
    private EquationIndexProvider indexProvider;
    private HybridSystem hybridSystem;

    private SimulationInitials simulationInitials;
    private List<Consumer<Double>> stepChangeListeners = new LinkedList<>();

    private boolean parallel;

    private String intgServer;
    private int intgPort;
    private ClassLoader modelClassLoader;
    private final String resultFileName;

    private final boolean eventDetectionEnabled;
    private final double eventDetectionGamma;
    private final double eventDetectionStepBoundLow;

    // TODO: потом убрать, этот конструктор требуется только для minor.beditor
    public Controller(HSM hsm, CauchyInitials initials, IntgMethod method) {
        this.hsm = hsm;
        this.method = method;
        this.resultFileName = null;
        this.simulationInitials = new SimulationInitials(
                initials.getY0(), initials.getStepSize(), initials.getStart(), initials.getEnd());
        this.eventDetectionEnabled = false;
        this.eventDetectionGamma = 0.0;
        this.eventDetectionStepBoundLow = Double.MIN_VALUE;
    }

    public Controller(HSM hsm, CauchyInitials initials, IntgMethod method,
                      boolean parallel, String intgServer, int intgPort,
                      String resultFileName,
                      boolean eventDetectionEnabled, double eventDetectionGamma, double eventDetectionStepBoundLow) {
        this.hsm = hsm;
        this.simulationInitials = new SimulationInitials(
                initials.getY0(), initials.getStepSize(), initials.getStart(), initials.getEnd());
        this.method = method;
        this.parallel = parallel;
        this.intgServer = intgServer;
        this.intgPort = intgPort;
        this.resultFileName = resultFileName;
        this.eventDetectionEnabled = eventDetectionEnabled;
        this.eventDetectionGamma = eventDetectionGamma;
        this.eventDetectionStepBoundLow = eventDetectionStepBoundLow;
    }

    public HybridSystemIntgResult simulate() {
        checkHSM();
        prepareSimulation();
        return runSimulation();
    }

    /**
     * 1 Проверить на корректность
     */
    private void checkHSM() { // todo
    }

    /**
     * 2 Подготовить HSM к рассчетам
     */
    private void prepareSimulation() {
        indexProvider = new EquationIndexProvider(hsm);

        AnalyzedHybridSystemClassBuilder hsClassBuilder =
                new AnalyzedHybridSystemClassBuilder(hsm, indexProvider, DEFAULT_PACKAGE_NAME, DEFAULT_CLASS_NAME);
        String hsSourceCode = hsClassBuilder.buildSourceCode();

        hybridSystem = new SourceCodeCompiler<HybridSystem>().compile(
                DEFAULT_PACKAGE_NAME, DEFAULT_CLASS_NAME, hsSourceCode, true);
        modelClassLoader = hybridSystem.getClass().getClassLoader();

        // подготовка СЛАУ
        //HMLinearSystem linearSystem = hsm.getLinearSystem();
        //if (linearSystem != null && !linearSystem.isEmpty())
        //linearSystem.prepareForCalculation(modelContext);
        //hybridSystem.setLinearSystem(hsm.getLinearSystem());

        // заполняем начальные значения для ДУ
        double[] odeInitials = new double[hsm.getVariableTable().getOdes().size()];
        for (HMDerivativeEquation ode : hsm.getVariableTable().getOdes()) {
            int idx = indexProvider.getDifferentialEquationIndex(ode.getCode());
            odeInitials[idx] = ode.getInitialValue();
        }

        simulationInitials = new SimulationInitials(odeInitials, simulationInitials.getStep(),
                simulationInitials.getStart(), simulationInitials.getEnd());
    }

    /**
     * Моделирование
     */
    private HybridSystemIntgResult runSimulation() {
        ComputeEngineClient computeEngineClient = null;
        try {
            DaeSystemStepSolver stepSolver = new DefaultDaeSystemStepSolver(method, hybridSystem.getDaeSystem());

            if (parallel) {
                computeEngineClient = new ComputeEngineClient(modelClassLoader);
                computeEngineClient.connect(intgServer, intgPort);
                computeEngineClient.loadIntgMethod(method);
                computeEngineClient.loadDaeSystem(hybridSystem.getDaeSystem());
                stepSolver = new RemoteDaeSystemStepSolver(method, computeEngineClient);
            }

            HybridSystemSimulator cauchyProblemSolver = new HybridSystemSimulator();
            stepChangeListeners.forEach(cauchyProblemSolver::addStepChangeListener);

            EventDetectionIntgController eventDetector = new EventDetectionIntgController(eventDetectionGamma);
            eventDetector.setEnabled(eventDetectionEnabled);

            if (resultFileName != null) {
                return runSimulationWithResultFile(cauchyProblemSolver, stepSolver, eventDetector, eventDetectionStepBoundLow);
            }
            return runSimulationInMemory(cauchyProblemSolver, stepSolver, eventDetector, eventDetectionStepBoundLow);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (computeEngineClient != null) {
                computeEngineClient.disconnect();
            }
        }
    }

    private HybridSystemIntgResult runSimulationInMemory(HybridSystemSimulator cauchyProblemSolver,
                                                         DaeSystemStepSolver stepSolver,
                                                         EventDetectionIntgController eventDetector,
                                                         double eventDetectionStepBoundLow) {
        IntgResultMemoryStore resultMemoryStore = new IntgResultMemoryStore();
        IntgMetricData metricData = cauchyProblemSolver.run(
                hybridSystem, stepSolver, simulationInitials, eventDetector, eventDetectionStepBoundLow, resultMemoryStore);

        logCalculationStatistic(metricData, stepSolver);

        HybridSystemIntgResult result = new HybridSystemIntgResult();
        result.setMetricData(metricData);
        result.setResultPointProvider(resultMemoryStore);
        result.setEquationIndexProvider(indexProvider);
        return result;
    }

    private HybridSystemIntgResult runSimulationWithResultFile(HybridSystemSimulator cauchyProblemSolver,
                                                               DaeSystemStepSolver stepSolver,
                                                               EventDetectionIntgController eventDetector,
                                                               double eventDetectionStepBoundLow) throws IOException {
        IntgMetricData metricData;
        try(IntgResultPointFileWriter resultWriter = new IntgResultPointFileWriter(resultFileName)) {
            //resultWriter.open();
            metricData = cauchyProblemSolver.run(hybridSystem, stepSolver, simulationInitials,
                    eventDetector, eventDetectionStepBoundLow, resultWriter);
            resultWriter.await();
        }

        logCalculationStatistic(metricData, stepSolver);

        IntgResultPointFileReader resultReader = new IntgResultPointFileReader();

        HybridSystemIntgResult result = new HybridSystemIntgResult();
        result.setMetricData(metricData);
        result.setResultPointProvider(resultReader);
        result.setEquationIndexProvider(indexProvider);
        return result;
    }

    private void logCalculationStatistic(IntgMetricData metricData, DaeSystemStepSolver stepSolver) {
        if (logger.isInfoEnabled()) {
            long stepCalculationCount = stepSolver instanceof DefaultDaeSystemStepSolver ?
                    ((DefaultDaeSystemStepSolver) stepSolver).getStepCalculationCount() : -1;
            long rhsCalculationCount = stepSolver instanceof DefaultDaeSystemStepSolver ?
                    ((DefaultDaeSystemStepSolver) stepSolver).getRhsCalculationCount() : -1;
            logger.info("Simulation time: {} ms; Step calculation count: {}; RHS calculation count: {}",
                    metricData.getSimulationTime(), stepCalculationCount, rhsCalculationCount);
        }
    }

    public void addStepChangeListener(Consumer<Double> c) {
        stepChangeListeners.add(c);
    }

}
