package ru.nstu.isma.server.infrastructure.simulation

import ru.isma.next.exchange.format.writeAll
import ru.nstu.isma.compiler.hsm.core.HSM
import ru.nstu.isma.compiler.hsm.core.IHSM
import ru.nstu.isma.domain.handlers.runSimulation.RunSimulationParameters
import ru.nstu.isma.domain.simulation.ISimulationExecutor
import ru.nstu.isma.domain.simulation.ISimulationSessionStore
import ru.nstu.isma.domain.simulation.SimulationStatus
import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue

class SimulationExecutorImpl(
    private val engine: ISimulationEngine,
    private val sessionStore: ISimulationSessionStore,
    private val executorService: ExecutorService,
) : ISimulationExecutor {

    override fun execute(simulationId: Long, parameters: RunSimulationParameters, model: IHSM) {
        executorService.submit {
            try {
                runSimulation(simulationId, parameters, model as HSM)
            } catch (e: InterruptedException) {
                val session = sessionStore.get(simulationId)
                if (session?.status != SimulationStatus.CANCELLED) {
                    sessionStore.failSimulation(simulationId, e.message ?: "Simulation interrupted")
                }
            } catch (e: Throwable) {
                logger.error("Simulation $simulationId failed", e)
                sessionStore.failSimulation(simulationId, e.message ?: "Unknown error")
            }
        }
    }

    private fun runSimulation(simulationId: Long, parameters: RunSimulationParameters, hsm: HSM) {
        val prepared = engine.prepare(hsm, parameters)
        val tempFile = File.createTempFile("isma_simulation_$simulationId", ".bin")
        val pointQueue = LinkedBlockingQueue<QueueItem>()

        val writerThread = Thread.ofVirtual().start {
            writeAll(tempFile, prepared.columnNames, pointsSequence(pointQueue))
        }

        var error: Throwable? = null
        try {
            engine.run(
                prepared,
                onProgress = { currentTime -> sessionStore.updateProgress(simulationId, currentTime) },
                isCancelled = { sessionStore.get(simulationId)?.status == SimulationStatus.CANCELLED },
                onPoint = { point -> pointQueue.put(QueueItem.Point(point)) },
            )
        } catch (e: Throwable) {
            error = e
        } finally {
            pointQueue.put(QueueItem.EndOfStream)
        }
        writerThread.join()
        if (error != null) {
            tempFile.delete()
            throw error
        }
        sessionStore.completeSimulation(simulationId, tempFile.absolutePath)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SimulationExecutorImpl::class.java)
    }

    private fun pointsSequence(queue: LinkedBlockingQueue<QueueItem>): Sequence<DoubleArray> = sequence {
        while (true) {
            val item = queue.take()
            if (item is QueueItem.EndOfStream) break
            yield((item as QueueItem.Point).point)
        }
    }

    private sealed class QueueItem {
        data class Point(val point: DoubleArray) : QueueItem()
        data object EndOfStream : QueueItem()
    }
}
