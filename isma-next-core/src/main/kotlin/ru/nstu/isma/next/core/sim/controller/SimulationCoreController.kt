package ru.nstu.isma.next.core.sim.controller

import org.slf4j.LoggerFactory
import ru.nstu.isma.core.hsm.HSM
import ru.nstu.isma.intg.api.IntgMetricData
import ru.nstu.isma.intg.api.IntgResultMemoryStore
import ru.nstu.isma.intg.api.IntgResultPointFileReader
import ru.nstu.isma.intg.api.IntgResultPointFileWriter
import ru.nstu.isma.intg.api.calcmodel.HybridSystem
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyInitials
import ru.nstu.isma.intg.api.methods.IntgMethod
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver
import ru.nstu.isma.intg.core.methods.EventDetectionIntgController
import ru.nstu.isma.intg.core.solvers.DefaultDaeSystemStepSolver
import ru.nstu.isma.intg.server.client.ComputeEngineClient
import ru.nstu.isma.intg.server.client.RemoteDaeSystemStepSolver
import ru.nstu.isma.next.core.sim.controller.gen.AnalyzedHybridSystemClassBuilder
import ru.nstu.isma.next.core.sim.controller.gen.EquationIndexProvider
import ru.nstu.isma.next.core.sim.controller.gen.SourceCodeCompiler
import java.io.IOException
import java.util.*
import java.util.function.Consumer

/**
 * Created by Bessonov Alex
 * on 04.01.2015.
 */
class SimulationCoreController(
        private var hsm: HSM, initials: CauchyInitials,
        private var method: IntgMethod,
        private var parallel: Boolean,
        private var intgServer: String?,
        private var intgPort: Int,
        private val resultFileName: String?,
        private val eventDetectionEnabled: Boolean,
        private val eventDetectionGamma: Double,
        private val eventDetectionStepBoundLow: Double) {

    private val logger = LoggerFactory.getLogger(this.javaClass)
    private var indexProvider: EquationIndexProvider? = null
    private var hybridSystem: HybridSystem? = null
    private var simulationInitials: SimulationInitials
    private val stepChangeListeners: MutableList<Consumer<Double>> = LinkedList()
    private var modelClassLoader: ClassLoader? = null

    init {
        simulationInitials = SimulationInitials(
                initials.y0, initials.stepSize, initials.start, initials.end)
    }

    fun simulate(): HybridSystemIntgResult {
        checkHSM()
        prepareSimulation()
        return runSimulation()
    }

    /**
     * 1 Проверить на корректность
     */
    private fun checkHSM() { // todo
    }

    /**
     * 2 Подготовить HSM к рассчетам
     */
    private fun prepareSimulation() {
        indexProvider = EquationIndexProvider(hsm)
        val hsClassBuilder = AnalyzedHybridSystemClassBuilder(hsm, indexProvider!!, DEFAULT_PACKAGE_NAME, DEFAULT_CLASS_NAME)
        val hsSourceCode = hsClassBuilder.buildSourceCode()
        hybridSystem = SourceCodeCompiler<HybridSystem>().compile(
                DEFAULT_PACKAGE_NAME, DEFAULT_CLASS_NAME, hsSourceCode, true)
        modelClassLoader = hybridSystem!!.javaClass.classLoader

        // подготовка СЛАУ
        //HMLinearSystem linearSystem = hsm.getLinearSystem();
        //if (linearSystem != null && !linearSystem.isEmpty())
        //linearSystem.prepareForCalculation(modelContext);
        //hybridSystem.setLinearSystem(hsm.getLinearSystem());

        // заполняем начальные значения для ДУ
        val odeInitials = DoubleArray(hsm.variableTable.odes.size)
        for (ode in hsm.variableTable.odes) {
            val idx = indexProvider!!.getDifferentialEquationIndex(ode.code)!!
            odeInitials[idx] = ode.initialValue
        }
        simulationInitials = SimulationInitials(odeInitials, simulationInitials.step,
                simulationInitials.start, simulationInitials.end)
    }

    /**
     * Моделирование
     */
    private fun runSimulation(): HybridSystemIntgResult {
        var computeEngineClient: ComputeEngineClient? = null
        return try {
            var stepSolver: DaeSystemStepSolver = DefaultDaeSystemStepSolver(method, hybridSystem?.daeSystem)
            if (parallel) {
                computeEngineClient = ComputeEngineClient(modelClassLoader)
                computeEngineClient.connect(intgServer, intgPort)
                computeEngineClient.loadIntgMethod(method)
                computeEngineClient.loadDaeSystem(hybridSystem!!.daeSystem)
                stepSolver = RemoteDaeSystemStepSolver(method, computeEngineClient)
            }
            val cauchyProblemSolver = HybridSystemSimulator()
            stepChangeListeners.forEach(Consumer { c: Consumer<Double> -> cauchyProblemSolver.addStepChangeListener(c) })
            val eventDetector = EventDetectionIntgController(eventDetectionGamma)
            eventDetector.isEnabled = eventDetectionEnabled
            if (resultFileName != null) {
                runSimulationWithResultFile(cauchyProblemSolver, stepSolver, eventDetector, eventDetectionStepBoundLow)
            } else runSimulationInMemory(cauchyProblemSolver, stepSolver, eventDetector, eventDetectionStepBoundLow)
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException(e)
        } finally {
            if (computeEngineClient != null) {
                computeEngineClient.disconnect()
            }
        }
    }

    private fun runSimulationInMemory(cauchyProblemSolver: HybridSystemSimulator,
                                      stepSolver: DaeSystemStepSolver,
                                      eventDetector: EventDetectionIntgController,
                                      eventDetectionStepBoundLow: Double): HybridSystemIntgResult {
        val resultMemoryStore = IntgResultMemoryStore()
        val metricData: IntgMetricData? = cauchyProblemSolver.run(
                hybridSystem, stepSolver, simulationInitials, eventDetector, eventDetectionStepBoundLow, resultMemoryStore)
        logCalculationStatistic(metricData, stepSolver)
        return HybridSystemIntgResult(indexProvider, metricData, resultMemoryStore)
    }

    @Throws(IOException::class)
    private fun runSimulationWithResultFile(cauchyProblemSolver: HybridSystemSimulator,
                                            stepSolver: DaeSystemStepSolver,
                                            eventDetector: EventDetectionIntgController,
                                            eventDetectionStepBoundLow: Double): HybridSystemIntgResult {
        var metricData: IntgMetricData?
        val resultWriter = IntgResultPointFileWriter(resultFileName)
        metricData = cauchyProblemSolver.run(hybridSystem, stepSolver, simulationInitials,
                eventDetector, eventDetectionStepBoundLow, resultWriter)
        resultWriter.await()
        resultWriter.close()

        logCalculationStatistic(metricData, stepSolver)
        val resultReader = IntgResultPointFileReader()
        val result = HybridSystemIntgResult()
        result.metricData = metricData
        result.resultPointProvider = resultReader
        result.equationIndexProvider = indexProvider
        return result
    }

    private fun logCalculationStatistic(metricData: IntgMetricData?, stepSolver: DaeSystemStepSolver) {
        if (logger.isInfoEnabled) {
            val stepCalculationCount: Long = if (stepSolver is DefaultDaeSystemStepSolver) stepSolver.stepCalculationCount else -1
            val rhsCalculationCount: Long = if (stepSolver is DefaultDaeSystemStepSolver) stepSolver.rhsCalculationCount else -1
            logger.info("Simulation time: {} ms; Step calculation count: {}; RHS calculation count: {}",
                    metricData?.simulationTime, stepCalculationCount, rhsCalculationCount)
        }
    }

    fun addStepChangeListener(c: Consumer<Double>) {
        stepChangeListeners.add(c)
    }

    companion object {
        private const val DEFAULT_PACKAGE_NAME = "ru.nstu.isma.core.simulation.controller"
        private const val DEFAULT_CLASS_NAME = "AnalyzedHybridSystem"
    }
}