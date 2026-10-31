package ru.nstu.isma.compiler.cli

import ru.nstu.isma.lisma.compiler.LismaCompiler
import ru.nstu.isma.lisma.compiler.LismaCompilationResult
import ru.nstu.isma.lisma.compiler.LismaError
import java.io.File
import kotlin.system.exitProcess

/**
 * Standalone LISMA compiler CLI — a thin wrapper over [LismaCompiler].
 *
 * Reads LISMA source from stdin.
 *
 * **Compile mode (default):** compiles the model to a self-contained
 * `.class` file — the analyzed hybrid-system class with the translated HSM
 * inlined as a serialized blob (`__ISMA_HSM__`) — and writes it to stdout, or
 * to the file given by `--out <path>`. On translation failure the diagnostic
 * error lines are written to stdout and the exit code is non-zero.
 *
 * **Validate mode (`--validate`):** only translates the source. On success
 * nothing is written and the exit code is 0; on failure the diagnostic error
 * lines are written to stdout and the exit code is non-zero.
 *
 * Error line format (one per line): `<row>:<col>:<message>` where row/col are
 * -1 for errors without a source position.
 */
fun main(args: Array<String>) {
    val argList = args.toList()
    val validate = argList.contains("--validate")
    val outPath = argList.indexOf("--out").let { idx ->
        if (idx >= 0 && idx + 1 < argList.size) argList[idx + 1] else null
    }

    val source = System.`in`.bufferedReader().readText()
    if (source.isBlank()) {
        fail("empty input: expected LISMA source on stdin")
    }

    try {
        if (validate) {
            val errors = LismaCompiler.validate(source)
            if (errors.isEmpty()) return
            printlnErrorLines(errors)
            exitProcess(1)
            return
        }
        when (val result = LismaCompiler.compile(source)) {
            is LismaCompilationResult.Success -> {
                if (outPath != null) {
                    File(outPath).writeBytes(result.classBytes)
                } else {
                    System.out.write(result.classBytes)
                    System.out.flush()
                }
            }
            is LismaCompilationResult.Failure -> {
                printlnErrorLines(result.errors)
                exitProcess(1)
            }
        }
    } catch (e: IllegalArgumentException) {
        fail(e.message ?: "invalid input")
    } catch (e: IllegalStateException) {
        fail(e.message ?: "compilation failed")
    }
}

/** Writes one `<row>:<col>:<message>` line per error to stdout. */
private fun printlnErrorLines(errors: List<LismaError>) {
    errors.forEach { error ->
        System.out.println("${error.row}:${error.col}:${error.message}")
    }
    System.out.flush()
}

private fun fail(message: String): Nothing {
    System.err.println("lisma-compiler-cli: $message")
    exitProcess(1)
}
