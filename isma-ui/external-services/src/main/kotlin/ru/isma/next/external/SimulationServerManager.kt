package ru.isma.next.external

import org.slf4j.LoggerFactory
import java.io.File

class SimulationServerManager(
    private val scriptPath: String = resolveServerScriptPath(),
) {
    data class SocketPaths(
        val grpc: String,
        val http: String,
    )

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
    private var socketPaths: SocketPaths? = null
    @Volatile private var running = false

    fun start(): SocketPaths {
        if (running) return socketPaths!!

        val file = File(scriptPath)
        require(file.exists()) { "isma-server script not found at: $scriptPath" }

        process = ProcessBuilder(scriptPath)
            .redirectErrorStream(true)
            .start()

        val reader = process!!.inputStream.bufferedReader()
        val lines = mutableListOf<String>()
        
        while (lines.size < 4) {
            val line = reader.readLine() ?: break
            if (line.startsWith("WARNING:") || line.startsWith("SLF4J:") || line.isBlank()) continue
            if (line.contains(" INFO ") || line.contains(" WARN ")) continue
            lines.add(line)
        }

        if (lines.isEmpty()) {
            throw IllegalStateException("isma-server started but produced no output")
        }

        if (!lines[0].startsWith("Starting gRPC server on Unix socket:")) {
            throw IllegalStateException("Unexpected server output: ${lines[0]}")
        }

        val grpcSocket = lines[0].substringAfter(": ").trim()
        val httpSocket = lines.find { it.startsWith("HTTP_SOCKET=") }?.substringAfter("=")?.trim()
            ?: throw IllegalStateException("HTTP socket not found in server output")

        socketPaths = SocketPaths(grpc = grpcSocket, http = httpSocket)
        running = true
        logger.info("isma-server started on gRPC socket: ${socketPaths!!.grpc}")
        logger.info("isma-server started on HTTP socket: ${socketPaths!!.http}")

        Runtime.getRuntime().addShutdownHook(Thread { stop() })
        return socketPaths!!
    }

    fun stop() {
        if (!running) return
        running = false
        process?.destroy()
        process = null
        socketPaths = null
        logger.info("isma-server stopped")
    }

    fun isRunning(): Boolean = running
}
