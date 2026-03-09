package ru.nstu.isma.next.core.sim.controller.services.runners

import ru.nstu.isma.intg.api.models.IntgMetricData
import ru.nstu.isma.intg.api.models.IntgResultPoint
import ru.nstu.isma.intg.api.providers.AsyncFilePointProvider
import ru.nstu.isma.intg.api.utilities.IntegrationResultPointFileHelpers
import ru.nstu.isma.next.core.sim.controller.models.HybridSystemIntegrationResult
import ru.nstu.isma.next.core.sim.controller.models.HybridSystemSimulatorParameters
import ru.nstu.isma.next.core.sim.controller.models.SimulationParameters
import ru.nstu.isma.next.core.sim.controller.services.simulators.IHybridSystemSimulator
import java.io.File
import java.util.concurrent.LinkedBlockingQueue

sealed class QueueItem {
    data class Point(val point: IntgResultPoint) : QueueItem()
    data object EndOfStream : QueueItem()
}

class InFileSimulationRunner(
    private val hybridSystemSimulator: IHybridSystemSimulator,
) : ISimulationRunner {
    override fun run(context: SimulationParameters): HybridSystemIntegrationResult {
        val tempFile = File.createTempFile("ismaSolverTempFile_", ".txt")

        var result: IntgMetricData? = null
        val pointQueue = LinkedBlockingQueue<QueueItem>()

        val simulatorThread = Thread.ofVirtual().start {
            val simulatorParameters = HybridSystemSimulatorParameters(
                context.compilationResult,
                context.simulationInitials,
                stepChangeHandlers = context.stepChangeHandlers,
                resultPointHandlers = { point ->
                    pointQueue.put(QueueItem.Point(point))
                }
            )

            result = hybridSystemSimulator.runAsync(simulatorParameters)
            pointQueue.put(QueueItem.EndOfStream)
        }

        val writerThread = Thread.ofVirtual().start {
            tempFile.bufferedWriter().use { writer ->
                var isFirst = true
                while (true) {
                    val item = pointQueue.take()
                    if (item is QueueItem.EndOfStream) break
                    val point = (item as QueueItem.Point).point
                    if (isFirst) {
                        writer.append(IntegrationResultPointFileHelpers.buildCsvHeader(point))
                        isFirst = false
                    }
                    writer.append(IntegrationResultPointFileHelpers.buildCsvString(point))
                }
            }
        }

        simulatorThread.join()
        writerThread.join()

        val resultReader = AsyncFilePointProvider(tempFile)

        return HybridSystemIntegrationResult(
            metricData = result!!,
            resultPointProvider = resultReader,
            equationIndexProvider = context.compilationResult.indexProvider,
        )
    }
}
