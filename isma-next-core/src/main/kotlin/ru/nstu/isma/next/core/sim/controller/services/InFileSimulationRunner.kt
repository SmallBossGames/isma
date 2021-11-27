package ru.nstu.isma.next.core.sim.controller.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
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
) : ISimulationRunner {
    override suspend fun run(context: SimulationParameters): HybridSystemIntegrationResult = coroutineScope {
        val tempFileCreating = async(Dispatchers.IO) {
            File.createTempFile("ismaSolverTempFile_", ".txt")
        }

        val pointsChannel = Channel<IntgResultPoint>()

        val simulating = async {
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

        val tempFile = tempFileCreating.await()

        launch(Dispatchers.IO) {
            tempFile.bufferedWriter().use { writer ->
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

        val metricData = simulating.await()
        val resultReader = AsyncFilePointProvider(tempFile)

        return@coroutineScope HybridSystemIntegrationResult(
            metricData = metricData,
            resultPointProvider = resultReader,
            equationIndexProvider = context.compilationResult.indexProvider,
        )
    }
}