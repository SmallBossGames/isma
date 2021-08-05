package ru.nstu.isma.next.core.sim.controller

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
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
import ru.nstu.isma.next.core.sim.controller.parameters.EventDetectionParameters
import ru.nstu.isma.next.core.sim.controller.parameters.ParallelParameters
import java.io.IOException
import java.util.*
import java.util.function.Consumer

/**
 * Created by Bessonov Alex
 * on 04.01.2015.
 */
class SimulationCoreController(
        private val hsm: HSM,
        initials: CauchyInitials,
        private val method: IntgMethod,
        private val parallelParameters: ParallelParameters?,
        private val resultFileName: String?,
        private val eventDetectionParameters: EventDetectionParameters?) {

    private val logger = LoggerFactory.getLogger(this.javaClass)
    private lateinit var indexProvider: EquationIndexProvider
    private lateinit var hybridSystem: HybridSystem
    private var simulationInitials: SimulationInitials
    private val stepChangeListeners = ArrayList<(value: Double) -> Unit>()
    private var modelClassLoader: ClassLoader? = null

    init {
        simulationInitials = SimulationInitials(
                initials.y0, initials.stepSize, initials.start, initials.end)
    }

    suspend fun simulateAsync(): HybridSystemIntegrationResult = coroutineScope {
        checkHSM()
        prepareSimulation()
        return@coroutineScope runSimulationAsync()
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
        val hsClassBuilder = AnalyzedHybridSystemClassBuilder(hsm, indexProvider, DEFAULT_PACKAGE_NAME, DEFAULT_CLASS_NAME)
        val hsSourceCode = hsClassBuilder.buildSourceCode()
        hybridSystem = SourceCodeCompiler<HybridSystem>().compile(
            DEFAULT_PACKAGE_NAME, DEFAULT_CLASS_NAME, hsSourceCode
        )
        modelClassLoader = hybridSystem.javaClass.classLoader

        // подготовка СЛАУ
        //HMLinearSystem linearSystem = hsm.getLinearSystem();
        //if (linearSystem != null && !linearSystem.isEmpty())
        //linearSystem.prepareForCalculation(modelContext);
        //hybridSystem.setLinearSystem(hsm.getLinearSystem());

        // заполняем начальные значения для ДУ
        val odeInitials = DoubleArray(hsm.variableTable.odes.size)
        for (ode in hsm.variableTable.odes) {
            val idx = indexProvider.getDifferentialEquationIndex(ode.code)!!
            odeInitials[idx] = ode.initialValue
        }
        simulationInitials = SimulationInitials(odeInitials, simulationInitials.step,
                simulationInitials.start, simulationInitials.end)
    }

    /**
     * Моделирование
     */
    private suspend fun runSimulationAsync(): HybridSystemIntegrationResult = coroutineScope {
        var computeEngineClient: ComputeEngineClient? = null
        return@coroutineScope try {
            val stepSolver: DaeSystemStepSolver = if (parallelParameters != null) {
                computeEngineClient = ComputeEngineClient(modelClassLoader)
                computeEngineClient.connect(parallelParameters.server, parallelParameters.port)
                computeEngineClient.loadIntgMethod(method)
                computeEngineClient.loadDaeSystem(hybridSystem.daeSystem)
                RemoteDaeSystemStepSolver(method, computeEngineClient)
            } else {
                DefaultDaeSystemStepSolver(method, hybridSystem.daeSystem)
            }

            val cauchyProblemSolver = HybridSystemSimulator()
            stepChangeListeners.forEach(Consumer { c -> cauchyProblemSolver.addStepChangeListener(c) })

            val eventDetector = if (eventDetectionParameters != null)
                EventDetectionIntgController(eventDetectionParameters.gamma, true)
            else
                EventDetectionIntgController(0.0, false)

            val stepBoundLow = eventDetectionParameters?.stepBoundLow ?: 0.0

            if (resultFileName != null) {
                runSimulationWithResultFile(cauchyProblemSolver, stepSolver, eventDetector, stepBoundLow)
            } else{
                runSimulationInMemory(cauchyProblemSolver, stepSolver, eventDetector, stepBoundLow)
            }
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            e.printStackTrace()
            throw RuntimeException(e)
        } finally {
            computeEngineClient?.disconnect()
        }
    }

    private suspend fun runSimulationInMemory(
        cauchyProblemSolver: HybridSystemSimulator,
        stepSolver: DaeSystemStepSolver,
        eventDetector: EventDetectionIntgController,
        eventDetectionStepBoundLow: Double
    ): HybridSystemIntegrationResult = coroutineScope {

        val resultMemoryStore = IntgResultMemoryStore()

        val metricData: IntgMetricData = cauchyProblemSolver.runAsync(
            hybridSystem,
            stepSolver,
            simulationInitials,
            eventDetector,
            eventDetectionStepBoundLow,
        ) {
            resultMemoryStore.accept(it)
        }

        logCalculationStatistic(metricData, stepSolver)
        return@coroutineScope HybridSystemIntegrationResult(indexProvider, metricData, resultMemoryStore)
    }

    @Throws(IOException::class)
    private suspend fun runSimulationWithResultFile(
        cauchyProblemSolver: HybridSystemSimulator,
        stepSolver: DaeSystemStepSolver,
        eventDetector: EventDetectionIntgController,
        eventDetectionStepBoundLow: Double
    ): HybridSystemIntegrationResult = coroutineScope {

        val resultWriter = IntgResultPointFileWriter(resultFileName)
        val resultReader = IntgResultPointFileReader()

        val metricData = cauchyProblemSolver.runAsync(
            hybridSystem,
            stepSolver,
            simulationInitials,
            eventDetector,
            eventDetectionStepBoundLow,
        ) {
            resultWriter.accept(it)
        }
        resultWriter.await()
        resultWriter.close()

        logCalculationStatistic(metricData, stepSolver)

        return@coroutineScope HybridSystemIntegrationResult(
            metricData = metricData,
            resultPointProvider = resultReader,
            equationIndexProvider = indexProvider,
        )
    }

    private fun logCalculationStatistic(
        metricData: IntgMetricData?,
        stepSolver: DaeSystemStepSolver
    ) {
        if (logger.isInfoEnabled) {
            val stepCalculationCount = if (stepSolver is DefaultDaeSystemStepSolver) stepSolver.stepCalculationCount else -1
            val rhsCalculationCount: Long = if (stepSolver is DefaultDaeSystemStepSolver) stepSolver.rhsCalculationCount else -1
            logger.info(
                "Simulation time: {} ms; Step calculation count: {}; RHS calculation count: {}",
                metricData?.simulationTime,
                stepCalculationCount,
                rhsCalculationCount)
        }
    }

    fun addStepChangeListener(c: (Double) -> Unit) {
        stepChangeListeners.add(c)
    }

    companion object {
        private const val DEFAULT_PACKAGE_NAME = "ru.nstu.isma.core.simulation.controller"
        private const val DEFAULT_CLASS_NAME = "AnalyzedHybridSystem"
    }
}