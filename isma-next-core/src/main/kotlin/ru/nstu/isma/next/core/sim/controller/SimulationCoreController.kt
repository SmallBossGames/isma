package ru.nstu.isma.next.core.sim.controller

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import org.slf4j.LoggerFactory
import ru.nstu.isma.core.hsm.HSM
import ru.nstu.isma.intg.api.IntgMetricData
import ru.nstu.isma.intg.api.models.IntgResultPoint
import ru.nstu.isma.intg.api.providers.AsyncFilePointProvider
import ru.nstu.isma.intg.api.providers.MemoryPointProvider
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver
import ru.nstu.isma.intg.api.utilities.IntegrationResultPointFileHelpers
import ru.nstu.isma.intg.core.methods.EventDetectionIntgController
import ru.nstu.isma.intg.core.solvers.DefaultDaeSystemStepSolver
import ru.nstu.isma.intg.server.client.ComputeEngineClient
import ru.nstu.isma.intg.server.client.RemoteDaeSystemStepSolver
import ru.nstu.isma.next.core.sim.controller.contracts.IHsmCompiler
import ru.nstu.isma.next.core.sim.controller.contracts.IHybridSystemSimulator
import ru.nstu.isma.next.core.sim.controller.contracts.ISimulationCoreController
import ru.nstu.isma.next.core.sim.controller.gen.EquationIndexProvider
import ru.nstu.isma.next.core.sim.controller.models.*
import java.io.File
import java.io.IOException

/**
 * Created by Bessonov Alex
 * on 04.01.2015.
 */
class SimulationCoreController(
    private val hybridSystemSimulator: IHybridSystemSimulator,
    private val hsmCompiler: IHsmCompiler,
) : ISimulationCoreController {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    /**
     * Моделирование
     */
    override suspend fun simulateAsync(parameters: IntegratorApiParameters): HybridSystemIntegrationResult = coroutineScope {
        val compilationResult = hsmCompiler.compile(parameters.hsm)

        val initials = SimulationInitials(
            differentialEquationInitials = createOdeInitials(compilationResult.indexProvider, parameters.hsm),
            start = parameters.initials.start,
            end = parameters.initials.end,
            step = parameters.initials.stepSize
        )

        var computeEngineClient: ComputeEngineClient? = null
        return@coroutineScope try {
            val stepSolver: DaeSystemStepSolver = if (parameters.parallelParameters != null) {
                computeEngineClient = ComputeEngineClient(compilationResult.classLoader).apply {
                    connect(parameters.parallelParameters.server, parameters.parallelParameters.port)
                    loadIntgMethod(parameters.method)
                    loadDaeSystem(compilationResult.hybridSystem.daeSystem)
                }
                RemoteDaeSystemStepSolver(parameters.method, computeEngineClient)
            } else {
                DefaultDaeSystemStepSolver(parameters.method, compilationResult.hybridSystem.daeSystem)
            }

            val eventDetector = if (parameters.eventDetectionParameters != null)
                EventDetectionIntgController(parameters.eventDetectionParameters.gamma, true)
            else
                EventDetectionIntgController(0.0, false)

            val stepBoundLow = parameters.eventDetectionParameters?.stepBoundLow ?: 0.0

            when(parameters.resultStorageType) {
                is MemoryStorage -> {
                    val context = InMemorySimulationParameters(
                        compilationResult.hybridSystem,
                        initials,
                        compilationResult.indexProvider,
                        stepSolver,
                        eventDetector,
                        stepBoundLow,
                        parameters.stepChangeHandlers
                    )

                    runSimulationInMemory(context)
                }
                is FileStorage -> {
                    val context = FileStorageSimulationParameters(
                        compilationResult.hybridSystem,
                        initials,
                        compilationResult.indexProvider,
                        stepSolver,
                        eventDetector,
                        stepBoundLow,
                        parameters.resultStorageType.filePath,
                        parameters.stepChangeHandlers
                    )

                    runSimulationWithResultFile(context)
                }
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

    private suspend fun createOdeInitials(indexProvider: EquationIndexProvider, hsm: HSM): DoubleArray = coroutineScope {
        val odeInitials = DoubleArray(hsm.variableTable.odes.size)

        for (ode in hsm.variableTable.odes) {
            val idx = indexProvider.getDifferentialEquationIndex(ode.code)!!
            odeInitials[idx] = ode.initialValue
        }

        return@coroutineScope odeInitials
    }

    private suspend fun runSimulationInMemory(context: InMemorySimulationParameters): HybridSystemIntegrationResult =
        coroutineScope {
            val resultMemoryStore = MemoryPointProvider()

            val simulatorParameters = HybridSystemSimulatorParameters(
                context.hybridSystem,
                context.stepSolver,
                context.simulationInitials,
                context.eventDetector,
                context.eventDetectionStepBoundLow,
                stepChangeHandlers = context.stepChangeHandlers,
                resultPointHandlers = arrayListOf(
                    {
                        resultMemoryStore.accept(it)
                    }
                )
            )

            val metricData: IntgMetricData = hybridSystemSimulator.runAsync(simulatorParameters)

            logCalculationStatistic(metricData, context.stepSolver)

            return@coroutineScope HybridSystemIntegrationResult(context.indexProvider, metricData, resultMemoryStore)
        }

    @Throws(IOException::class)
    private suspend fun runSimulationWithResultFile(context: FileStorageSimulationParameters): HybridSystemIntegrationResult =
        coroutineScope {
            val pointsChannel = Channel<IntgResultPoint>()

            val metricDataJob = async {
                val simulatorParameters = HybridSystemSimulatorParameters(
                    context.hybridSystem,
                    context.stepSolver,
                    context.simulationInitials,
                    context.eventDetector,
                    context.eventDetectionStepBoundLow,
                    stepChangeHandlers = context.stepChangeHandlers,
                    resultPointHandlers = arrayListOf(
                        {
                            pointsChannel.send(it)
                        }
                    )
                )

                val result = hybridSystemSimulator.runAsync(simulatorParameters)
                pointsChannel.close()
                return@async result
            }

            launch(Dispatchers.IO) {
                File(context.resultFileName).bufferedWriter().use { writer ->
                    var isFirst = true
                    pointsChannel.consumeEach {
                        if (isFirst) {
                            writer.append(IntegrationResultPointFileHelpers.buildCsvHeader(it))
                            isFirst = false
                        }
                        writer.append(IntegrationResultPointFileHelpers.buildCsvString(it))
                    }
                }
            }

            val metricData = metricDataJob.await()
            val resultReader = AsyncFilePointProvider(context.resultFileName)

            logCalculationStatistic(metricData, context.stepSolver)

            return@coroutineScope HybridSystemIntegrationResult(
                metricData = metricData,
                resultPointProvider = resultReader,
                equationIndexProvider = context.indexProvider,
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
}