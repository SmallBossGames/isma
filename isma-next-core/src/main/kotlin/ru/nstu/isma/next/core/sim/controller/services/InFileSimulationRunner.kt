package ru.nstu.isma.next.core.sim.controller.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import ru.nstu.isma.intg.api.models.IntgResultPoint
import ru.nstu.isma.intg.api.providers.AsyncFilePointProvider
import ru.nstu.isma.intg.api.utilities.IntegrationResultPointFileHelpers
import ru.nstu.isma.next.core.sim.controller.HybridSystemIntegrationResult
import ru.nstu.isma.next.core.sim.controller.contracts.IHybridSystemSimulator
import ru.nstu.isma.next.core.sim.controller.models.HybridSystemSimulatorParameters
import ru.nstu.isma.next.core.sim.controller.models.SimulationParameters
import java.io.File

class InFileSimulationRunner(
    private val hybridSystemSimulator: IHybridSystemSimulator,
    private val resultFileName: String,
) : ISimulationRunner {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override suspend fun run(context: SimulationParameters): HybridSystemIntegrationResult = coroutineScope {
        val pointsChannel = Channel<IntgResultPoint>()

        val metricDataJob = async {
            val simulatorParameters = HybridSystemSimulatorParameters(
                context.compilationResult,
                context.simulationInitials,
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
            File(resultFileName).bufferedWriter().use { writer ->
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
        val resultReader = AsyncFilePointProvider(resultFileName)

       /* logCalculationStatistic(metricData, context.stepSolver)*/

        return@coroutineScope HybridSystemIntegrationResult(
            metricData = metricData,
            resultPointProvider = resultReader,
            equationIndexProvider = context.compilationResult.indexProvider,
        )
    }

/*    private fun logCalculationStatistic(
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
    }*/
}