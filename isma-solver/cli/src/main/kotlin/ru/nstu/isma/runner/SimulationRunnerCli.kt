package ru.nstu.isma.runner

import org.slf4j.LoggerFactory
import ru.nstu.isma.next.integration.services.IntegrationMethodLibraryLoader
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.system.exitProcess

/**
 * Standalone simulation runner CLI.
 *
 * Accepts a compiled-model `.class` file (produced by the LISMA compiler CLI,
 * with the HSM inlined as a blob) and simulation parameters, runs the numerical
 * simulation, and streams the result to stdout in the ISMA exchange binary
 * format, one row per integration step. An orchestrator reads stdout to obtain
 * the result and to derive progress from the TIME column. The process stops on
 * SIGTERM/SIGINT (cancellation).
 *
 * Arguments:
 *   --list-methods        print the bundled integration method names (one per line) and exit
 *   --model <path>        path to the compiled-model .class file (required)
 *   --start <double>      simulation start time (required)
 *   --end <double>        simulation end time (required)
 *   --step <double>       initial integration step (required, positive)
 *   --method <name>       integration method name (required)
 *   --accuracy <double>   target accuracy (used when --accuracy-enabled)
 *   --accuracy-enabled    enable adaptive accuracy control
 *   --stability-enabled   enable stability control
 *   --gamma <double>      event-detection gamma (enables event detection with --low-border)
 *   --low-border <double> event-detection step lower bound
 */
fun main(args: Array<String>) {
    if (args.contains("--list-methods")) {
        listMethods()
        return
    }

    val params = try {
        RunnerParams.parse(args)
    } catch (e: IllegalArgumentException) {
        fail(e.message ?: "Invalid arguments")
    }

    val logger = LoggerFactory.getLogger("simulation-runner")
    logger.info(
        "Running simulation: model={} method={} start={} end={} step={}",
        params.model, params.method, params.start, params.end, params.step,
    )

    // Cancellation: a shutdown hook (triggered by SIGTERM/SIGINT) flips the flag;
    // the integration loop polls it on every step.
    val cancelled = AtomicBoolean(false)
    Runtime.getRuntime().addShutdownHook(Thread { cancelled.set(true) })

    val error = SimulationRunner(params, cancelled::get).run(File(params.model), System.out)
    if (error != null) {
        fail(error)
    }
    exitProcess(0)
}

/** Prints the bundled integration method names, one per line. */
private fun listMethods() {
    IntegrationMethodLibraryLoader.load().getIntegrationMethodNames().forEach(::println)
}

/** Prints [message] to stderr and exits with a non-zero status. */
private fun fail(message: String): Nothing {
    System.err.println("simulation-runner: $message")
    exitProcess(1)
}
