package ru.nstu.isma.daemon.infrastructure.translation

import org.slf4j.LoggerFactory
import ru.nstu.isma.domain.compiler.CompileOutcome
import ru.nstu.isma.domain.compiler.ILismaCompiler
import ru.nstu.isma.domain.handlers.compileLisma.CompilationError
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Compiles and validates LISMA source by running the standalone LISMA compiler
 * CLI as a separate process. The LISMA source goes to the CLI's stdin; in
 * compile mode the compiled `.class` comes back on stdout, in validate
 * mode the diagnostic error lines come back on stdout.
 *
 * CLI resolution order:
 * 1. `ISMA_COMPILER_CLI` environment variable or `isma.compiler.cli` system
 *    property — path to the CLI launch script, a jar file, or a directory
 *    containing the CLI jars.
 * 2. Fallback: the lib directory next to this application's jar (installDist
 *    layout), running the CLI main class via `java -cp`.
 */
class CliLismaCompiler(
    private val command: List<String> = resolveCliCommand(),
) : ILismaCompiler {

    override fun compile(sourceCode: String): CompileOutcome {
        val result = runCli(sourceCode, validate = false)
        return if (result.exitCode == 0) {
            CompileOutcome(artifact = result.stdout, errors = emptyList())
        } else {
            CompileOutcome(artifact = null, errors = parseErrorLines(result.stdout))
        }
    }

    override fun validate(sourceCode: String): List<CompilationError> {
        val result = runCli(sourceCode, validate = true)
        return if (result.exitCode == 0) {
            emptyList()
        } else {
            parseErrorLines(result.stdout)
        }
    }

    private fun runCli(sourceCode: String, validate: Boolean): CliResult {
        val command = if (validate) this.command + "--validate" else this.command
        val stderrFile = File.createTempFile("lisma-cli", ".err")
        return try {
            val process = ProcessBuilder(command)
                .redirectError(stderrFile)
                .start()

            process.outputStream.use { it.write(sourceCode.toByteArray()) }
            val stdout = process.inputStream.readBytes()
            val finished = process.waitFor(COMPILE_TIMEOUT_SECONDS, TimeUnit.SECONDS)

            if (!finished) {
                process.destroyForcibly()
                return CliResult(-1, stdout, "lisma-compiler-cli timed out after ${COMPILE_TIMEOUT_SECONDS}s")
            }

            val exitCode = process.exitValue()
            CliResult(exitCode, stdout, "")
        } catch (e: Exception) {
            CliResult(-1, ByteArray(0), e.message ?: e::class.java.name)
        } finally {
            stderrFile.delete()
        }
    }

    /** Parses `<row>:<col>:<message>` lines into [CompilationError]s. */
    private fun parseErrorLines(raw: ByteArray): List<CompilationError> {
        val text = String(raw, Charsets.UTF_8).trim()
        if (text.isEmpty()) return emptyList()
        return text.lineSequence().filter { it.isNotBlank() }.map { line ->
            val firstColon = line.indexOf(':')
            val secondColon = if (firstColon >= 0) line.indexOf(':', firstColon + 1) else -1
            if (firstColon >= 0 && secondColon >= 0) {
                val row = line.substring(0, firstColon).toIntOrNull() ?: -1
                val col = line.substring(firstColon + 1, secondColon).toIntOrNull() ?: -1
                val message = line.substring(secondColon + 1)
                CompilationError(row, col, message)
            } else {
                CompilationError(-1, -1, line)
            }
        }.toList()
    }

    private class CliResult(
        val exitCode: Int,
        val stdout: ByteArray,
        val error: String,
    )

    companion object {
        private const val ENV_VAR = "ISMA_COMPILER_CLI"
        private const val PROP_NAME = "isma.compiler.cli"
        private const val CLI_MAIN_CLASS = "ru.nstu.isma.compiler.cli.LismaCompilerCliKt"
        private const val COMPILE_TIMEOUT_SECONDS = 60L

        private val logger = LoggerFactory.getLogger(CliLismaCompiler::class.java)

        private fun resolveCliCommand(): List<String> {
            val explicit = System.getenv(ENV_VAR) ?: System.getProperty(PROP_NAME)
            if (!explicit.isNullOrBlank()) {
                val path = File(explicit)
                val command = if (path.isDirectory || path.name.endsWith(".jar")) {
                    javaCommand("-cp", cliClasspathOf(path), CLI_MAIN_CLASS)
                } else {
                    listOf(explicit)
                }
                logger.debug("Using lisma-compiler-cli from $ENV_VAR/$PROP_NAME: $command")
                return command
            }
            val libDir = File(CliLismaCompiler::class.java.protectionDomain.codeSource.location.toURI())
                .parentFile
                ?.takeIf { dir ->
                    dir.isDirectory && dir.listFiles { f -> f.name.endsWith(".jar") }?.isNotEmpty() == true
                }
                ?: throw IllegalStateException(
                    "Cannot locate lisma-compiler-cli: set $ENV_VAR or $PROP_NAME " +
                        "to the CLI launch script, jar, or lib directory"
                )
            logger.debug("Using lisma-compiler-cli from lib dir: ${libDir.absolutePath}")
            return javaCommand("-cp", "${libDir.absolutePath}/*", CLI_MAIN_CLASS)
        }

        private fun cliClasspathOf(path: File): String =
            if (path.isDirectory) "${path.absolutePath}/*" else path.absolutePath

        private fun javaCommand(vararg args: String): List<String> {
            val java = File(System.getProperty("java.home"), "bin/java")
            return listOf(java.absolutePath) + args
        }
    }
}
