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

        return@coroutineScope HybridSystemIntegrationResult(
            metricData = metricData,
            resultPointProvider = resultReader,
            equationIndexProvider = context.compilationResult.indexProvider,
        )
    }
}