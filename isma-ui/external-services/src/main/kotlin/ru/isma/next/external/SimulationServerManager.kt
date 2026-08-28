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
        private var shutdownHookRegistered = false

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
    private var shutdownHook: Thread? = null
    private val runningLock = Any()
    @Volatile private var running = false

    fun start(): SocketPaths {
        synchronized(runningLock) {
            if (running) return socketPaths!!
        }

        val file = File(scriptPath)
        require(file.exists()) { "isma-server script not found at: $scriptPath" }

        process = ProcessBuilder(scriptPath)
            .redirectErrorStream(true)
            .start()

        val proc = process ?: throw IllegalStateException("Process not initialized")
        val reader = proc.inputStream.bufferedReader()
        val lines = mutableListOf<String>()

        val startTime = System.currentTimeMillis()
        val timeoutMs = 30_000L
        while (lines.size < 4) {
            val elapsed = System.currentTimeMillis() - startTime
            if (elapsed >= timeoutMs) {
                throw IllegalStateException("isma-server startup timed out after ${timeoutMs / 1000}s")
            }
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
        val httpSocket = lines.find { it.startsWith("HTTP_SOCKET=") }
            ?.substringAfter("=", "")
            ?.trim()
            ?: throw IllegalStateException("HTTP socket not found in server output")

        socketPaths = SocketPaths(grpc = grpcSocket, http = httpSocket)
        synchronized(runningLock) {
            running = true
        }
        logger.info("isma-server started on gRPC socket: ${socketPaths!!.grpc}")
        logger.info("isma-server started on HTTP socket: ${socketPaths!!.http}")

        if (!shutdownHookRegistered) {
            shutdownHook = Thread { stop() }
            Runtime.getRuntime().addShutdownHook(shutdownHook!!)
            shutdownHookRegistered = true
        }
        return socketPaths!!
    }

    fun stop() {
        synchronized(runningLock) {
            if (!running) return
            running = false
        }
        try {
            shutdownHook?.let { Runtime.getRuntime().removeShutdownHook(it) }
        } catch (e: IllegalArgumentException) {
            // Hook was already removed (by JVM during shutdown or by a previous stop() call)
        }
        shutdownHook = null
        process?.destroy()
        process = null
        socketPaths = null
        logger.info("isma-server stopped")
    }
}
