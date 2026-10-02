package ru.nstu.isma.daemon.infrastructure.simulation

import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Resolves and launches the batteries-included simulation-runner CLI. The runner
 * ships its own distribution (bin + lib, including the integration-method jars);
 * the daemon launches it as a self-contained process rather than relying on the
 * daemon's own classpath.
 *
 * Runner resolution order:
 * 1. `ISMA_RUNNER_CLI` environment variable or `isma.runner.cli` system property —
 *    path to the runner launch script, a jar file, or a lib directory.
 * 2. Fallback: the runner's batteries-included distribution embedded in the daemon's
 *    distribution (`install/app/runner/lib`), running the runner main class via
 *    `java -cp`.
 */
object RunnerCli {
    private const val ENV_VAR = "ISMA_RUNNER_CLI"
    private const val PROP_NAME = "isma.runner.cli"
    private const val RUNNER_MAIN_CLASS = "ru.nstu.isma.runner.SimulationRunnerCliKt"
    private const val LIST_TIMEOUT_SECONDS = 30L

    private val logger = LoggerFactory.getLogger(RunnerCli::class.java)

    /** Resolves the command to launch the runner (java -cp with the runner lib and main class). */
    fun resolveCommand(): List<String> {
        val explicit = System.getenv(ENV_VAR) ?: System.getProperty(PROP_NAME)
        if (!explicit.isNullOrBlank()) {
            val path = File(explicit)
            val command = if (path.isDirectory || path.name.endsWith(".jar")) {
                javaCommand("-cp", classpathOf(path), RUNNER_MAIN_CLASS)
            } else {
                listOf(explicit)
            }
            logger.debug("Using simulation-runner-cli from $ENV_VAR/$PROP_NAME: $command")
            return command
        }
        val runnerLib = File(RunnerCli::class.java.protectionDomain.codeSource.location.toURI())
            .parentFile // daemon's lib dir (this jar lives there)
            ?.parentFile // daemon's distribution dir
            ?.let { File(it, "runner/lib") }
            ?: throw IllegalStateException(
                "Cannot locate simulation-runner-cli: set $ENV_VAR or $PROP_NAME " +
                    "to the runner launch script, jar, or lib directory"
            )
        if (runnerLib.isDirectory && runnerLib.listFiles { f -> f.name.endsWith(".jar") }?.isNotEmpty() == true) {
            logger.debug("Using simulation-runner-cli from dist: ${runnerLib.absolutePath}")
            return javaCommand("-cp", "${runnerLib.absolutePath}/*", RUNNER_MAIN_CLASS)
        }
        throw IllegalStateException(
            "Cannot locate simulation-runner-cli: set $ENV_VAR or $PROP_NAME " +
                "to the runner launch script, jar, or lib directory"
        )
    }

    /**
     * Launches the runner with `--list-methods` and returns the integration method
     * names bundled with the runner (one per line on stdout).
     */
    fun listMethods(): List<String> {
        val process = ProcessBuilder(resolveCommand() + "--list-methods")
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()
        val names = process.inputStream.bufferedReader().readLines().filter { it.isNotBlank() }
        if (!process.waitFor(LIST_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
            process.destroyForcibly()
            throw IllegalStateException("simulation-runner --list-methods timed out after ${LIST_TIMEOUT_SECONDS}s")
        }
        return names
    }

    private fun classpathOf(path: File): String =
        if (path.isDirectory) "${path.absolutePath}/*" else path.absolutePath

    private fun javaCommand(vararg args: String): List<String> {
        val java = File(System.getProperty("java.home"), "bin/java")
        return listOf(java.absolutePath) + args
    }
}
