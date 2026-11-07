package ru.nstu.isma.daemon.infrastructure.simulation

import org.slf4j.LoggerFactory
import ru.nstu.isma.domain.compiler.CompiledModel
import ru.nstu.isma.domain.handlers.runSimulation.RunSimulationParameters
import ru.nstu.isma.domain.simulation.ISimulationExecutor
import ru.nstu.isma.domain.simulation.ISimulationProcessRegistry
import ru.nstu.isma.domain.simulation.ISimulationSessionStore
import ru.nstu.isma.domain.simulation.SimulationStatus
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.EOFException
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.ExecutorService

/**
 * Executes simulations by launching the standalone simulation-runner CLI as a
 * separate process. The compiled `.class` is written to a temp file and
 * passed to the runner; the runner streams the result to stdout in the ISMA
 * exchange binary format. This executor reads the runner's stdout to obtain the
 * result and to derive progress from the TIME column, and waits for the runner
 * process to exit.
 *
 * Runner resolution order:
 * 1. `ISMA_RUNNER_CLI` environment variable or `isma.runner.cli` system
 *    property — path to the runner launch script, a jar file, or a directory
 *    containing the runner jars.
 * 2. Fallback: the lib directory next to this application's jar (installDist
 *    layout), running the runner main class via `java -cp`.
 */
class SimulationExecutorImpl(
    private val sessionStore: ISimulationSessionStore,
    private val processRegistry: ISimulationProcessRegistry,
    private val executorService: ExecutorService,
    private val runnerCommand: List<String> = RunnerCli.resolveCommand(),
) : ISimulationExecutor {

    override fun execute(simulationId: Long, parameters: RunSimulationParameters, model: CompiledModel) {
        executorService.submit {
            try {
                runSimulation(simulationId, parameters, model)
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

    private fun runSimulation(simulationId: Long, parameters: RunSimulationParameters, model: CompiledModel) {
        val modelFile = File.createTempFile("isma_model_$simulationId", ".bin")
        val resultFile = File.createTempFile("isma_simulation_$simulationId", ".bin")
        val errFile = File.createTempFile("isma_runner_$simulationId", ".err")

        try {
            modelFile.writeBytes(model.artifact)

            val command = buildRunnerCommand(modelFile, parameters)
            val process = ProcessBuilder(command)
                .redirectError(errFile)
                .start()
            processRegistry.register(simulationId, process)

            // The runner streams the result to stdout in the ISMA exchange binary
            // format; progress is derived from the TIME column of each streamed
            // row. All stdout bytes are accumulated and written to the result
            // file once the process exits.
            val result = ByteArrayOutputStream()
            var lastProgress = parameters.startTime
            process.inputStream.use { stdout ->
                consumeRunnerOutput(stdout, result) { currentTime ->
                    if (currentTime > lastProgress) {
                        sessionStore.updateProgress(simulationId, currentTime)
                        lastProgress = currentTime
                    }
                }
            }

            val exitCode = process.waitFor()
            processRegistry.unregister(simulationId)

            val session = sessionStore.get(simulationId)
            when {
                session?.status == SimulationStatus.CANCELLED -> {
                    resultFile.delete()
                }
                exitCode == 0 -> {
                    resultFile.writeBytes(result.toByteArray())
                    sessionStore.completeSimulation(simulationId, resultFile.absolutePath)
                }
                else -> {
                    resultFile.delete()
                    val diagnostics = errFile.readText().trim()
                    sessionStore.failSimulation(
                        simulationId,
                        "simulation-runner exited with code $exitCode" +
                            if (diagnostics.isNotEmpty()) ": $diagnostics" else ""
                    )
                }
            }
        } finally {
            modelFile.delete()
            errFile.delete()
        }
    }

    private fun buildRunnerCommand(
        modelFile: File,
        params: RunSimulationParameters,
    ): List<String> {
        val cmd = runnerCommand.toMutableList()
        cmd += listOf(
            "--model", modelFile.absolutePath,
            "--start", params.startTime.toString(),
            "--end", params.endTime.toString(),
            "--step", params.initialStep.toString(),
            "--method", params.methodName,
        )
        if (params.isAccuracyInUse) {
            cmd += listOf("--accuracy", params.accuracy.toString(), "--accuracy-enabled")
        }
        if (params.isStabilityControlInUse) {
            cmd += "--stability-enabled"
        }
        params.eventDetectionGamma?.let { gamma ->
            params.eventDetectionLowBorder?.let { lowBorder ->
                cmd += listOf("--gamma", gamma.toString(), "--low-border", lowBorder.toString())
            }
        }
        return cmd
    }

    /**
     * Reads the runner's stdout, which carries the result in the ISMA exchange
     * binary format. The header (column count + names) is read first; then each
     * row is read and its first value (the TIME column) is reported via [onTime]
     * for progress tracking. Every byte read is also written to [result] so the
     * accumulated stream is the complete result payload.
     */
    private fun consumeRunnerOutput(
        stdout: InputStream,
        result: OutputStream,
        onTime: (time: Double) -> Unit,
    ) {
        val dis = DataInputStream(TeeInputStream(stdout, result))
        try {
            val columnCount = dis.readUnsignedShort()
            repeat(columnCount) {
                val len = dis.readUnsignedShort()
                dis.readNBytes(len)
            }
            while (true) {
                onTime(dis.readDouble())
                for (i in 1 until columnCount) {
                    dis.readDouble()
                }
            }
        } catch (_: EOFException) {
            // stream exhausted — the runner has finished writing
        }
    }

    /**
     * An [InputStream] that forwards every byte read from [source] to [sink],
     * so the result can be accumulated while the same bytes are parsed.
     */
    private class TeeInputStream(
        private val source: InputStream,
        private val sink: OutputStream,
    ) : InputStream() {
        override fun read(): Int {
            val b = source.read()
            if (b >= 0) sink.write(b)
            return b
        }

        override fun read(b: ByteArray, off: Int, len: Int): Int {
            val n = source.read(b, off, len)
            if (n > 0) sink.write(b, off, n)
            return n
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SimulationExecutorImpl::class.java)
    }
}
