package ru.isma.next.external

import org.slf4j.LoggerFactory
import java.io.File

class SimulationServerManager(
    private val scriptPath: String = resolveServerScriptPath(),
) {
    companion object {
        private const val ENV_VAR = "ISMA_SERVER_SCRIPT"
        private const val PROP_NAME = "isma.server.script"

        private fun resolveServerScriptPath(): String {
            return System.getenv(ENV_VAR)
                ?: System.getProperty(PROP_NAME)
                ?: throw IllegalStateException(
                    "Neither environment variable '$ENV_VAR' nor system property '$PROP_NAME' is set. " +
                    "One of them is required to point to the isma-server launch script."
                )
        }
    }
    private val logger = LoggerFactory.getLogger(SimulationServerManager::class.java)
    private var process: Process? = null
    private var socketPath: String? = null
    @Volatile private var running = false

    fun start(): String {
        if (running) return socketPath!!

        val file = File(scriptPath)
        require(file.exists()) { "isma-server script not found at: $scriptPath" }

        process = ProcessBuilder(scriptPath)
            .redirectErrorStream(true)
            .start()

        val reader = process!!.inputStream.bufferedReader()
        val line = reader.readLine()
            ?: throw IllegalStateException("isma-server started but produced no output")

        if (!line.startsWith("Starting gRPC server on Unix socket:")) {
            throw IllegalStateException("Unexpected server output: $line")
        }

        socketPath = line.substringAfter(": ").trim()
        running = true
        logger.info("isma-server started on socket: $socketPath")

        Runtime.getRuntime().addShutdownHook(Thread { stop() })
        return socketPath!!
    }

    fun stop() {
        if (!running) return
        running = false
        process?.destroy()
        process = null
        socketPath = null
        logger.info("isma-server stopped")
    }

    fun isRunning(): Boolean = running
}
