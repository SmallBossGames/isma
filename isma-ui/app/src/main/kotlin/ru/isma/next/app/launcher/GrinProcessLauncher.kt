package ru.isma.next.app.launcher

import org.slf4j.LoggerFactory
import java.io.File

class GrinProcessLauncher() {
    companion object {
        private const val ENV_VAR = "ISMA_GRIN_SCRIPT"
        private const val PROP_NAME = "isma.grin.script"

        fun resolveGrinScriptPath(): String? {
            return System.getenv(ENV_VAR)
                ?: System.getProperty(PROP_NAME)
        }
    }

    private val logger = LoggerFactory.getLogger(GrinProcessLauncher::class.java)
    private var process: Process? = null

    fun launch(resultFile: File, chartColumns: List<String>) {
        val scriptPath = resolveGrinScriptPath()
            ?: run {
                logger.error("Grin is not configured. Set $ENV_VAR environment variable or $PROP_NAME system property to enable chart display.")
                return
            }

        val file = File(scriptPath)
        if (!file.exists()) {
            logger.error("Grin script not found at: $scriptPath")
            return
        }

        val args = mutableListOf<String>()
        args.add("--result-file")
        args.add(resultFile.absolutePath)
        if (chartColumns.isNotEmpty()) {
            args.add("--charts")
            args.add(chartColumns.joinToString(","))
        }

        logger.info("Launching Grin with args: ${args.joinToString(" ")}")

        process = ProcessBuilder(scriptPath, *args.toTypedArray())
            .redirectErrorStream(true)
            .start()

        Runtime.getRuntime().addShutdownHook(Thread { stop() })
    }

    fun stop() {
        process?.destroy()
        process = null
        logger.info("Grin process stopped")
    }
}
