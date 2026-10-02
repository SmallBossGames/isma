package ru.nstu.isma.daemon.infrastructure.simulation

import org.slf4j.LoggerFactory
import ru.nstu.isma.domain.compiler.CompiledModel
import ru.nstu.isma.domain.handlers.runSimulation.RunSimulationParameters
import ru.nstu.isma.domain.simulation.ISimulationExecutor
import ru.nstu.isma.domain.simulation.ISimulationProcessRegistry
import ru.nstu.isma.domain.simulation.ISimulationSessionStore
import ru.nstu.isma.domain.simulation.SimulationStatus
import java.io.File
import java.util.concurrent.ExecutorService

/**
 * Executes simulations by launching the standalone simulation-runner CLI as a
 * separate process. The compiled `.class` is written to a temp file and
 * passed to the runner; the runner writes the result file and a progress file.
 * This executor polls the progress file to update the session and waits for the
 * runner process to exit.
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
        val outFile = File.createTempFile("isma_simulation_$simulationId", ".bin")
        val progressFile = File.createTempFile("isma_progress_$simulationId", ".txt")
        val errFile = File.createTempFile("isma_runner_$simulationId", ".err")

        try {
            modelFile.writeBytes(model.artifact)

            val command = buildRunnerCommand(modelFile, outFile, progressFile, parameters)
            val process = ProcessBuilder(command)
                .redirectError(errFile)
                .start()
            processRegistry.register(simulationId, process)

            var lastProgress = parameters.startTime
            while (process.isAlive) {
                readProgress(progressFile)?.let { currentTime ->
                    if (currentTime > lastProgress) {
                        sessionStore.updateProgress(simulationId, currentTime)
                        lastProgress = currentTime
                    }
                }
                Thread.sleep(POLL_INTERVAL_MS)
            }

            val exitCode = process.waitFor()
            processRegistry.unregister(simulationId)

            // Final progress read: the polling loop above may not have run if the
            // process exited before the first poll (fast simulations).
            readProgress(progressFile)?.let { currentTime ->
                if (currentTime > lastProgress) {
                    sessionStore.updateProgress(simulationId, currentTime)
                }
            }

            val session = sessionStore.get(simulationId)
            when {
                session?.status == SimulationStatus.CANCELLED -> {
                    outFile.delete()
                }
                exitCode == 0 -> {
                    sessionStore.completeSimulation(simulationId, outFile.absolutePath)
                }
                else -> {
                    outFile.delete()
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
            progressFile.delete()
            errFile.delete()
        }
    }

    private fun buildRunnerCommand(
        modelFile: File,
        outFile: File,
        progressFile: File,
        params: RunSimulationParameters,
    ): List<String> {
        val cmd = runnerCommand.toMutableList()
        cmd += listOf(
            "--model", modelFile.absolutePath,
            "--out", outFile.absolutePath,
            "--progress", progressFile.absolutePath,
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

    private fun readProgress(file: File): Double? {
        return try {
            val content = file.readText().trim()
            if (content.isEmpty()) null else content.toDouble()
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private const val POLL_INTERVAL_MS = 100L

        private val logger = LoggerFactory.getLogger(SimulationExecutorImpl::class.java)
    }
}
