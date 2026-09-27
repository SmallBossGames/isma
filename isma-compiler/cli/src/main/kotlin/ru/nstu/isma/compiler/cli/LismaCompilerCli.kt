package ru.nstu.isma.compiler.cli

import ru.nstu.isma.compiler.hsm.core.HSM
import ru.nstu.isma.compiler.hsm.core.models.IsmaError
import ru.nstu.isma.compiler.hsm.core.models.IsmaErrorList
import ru.nstu.isma.compiler.hsm.core.models.IsmaSemanticError
import ru.nstu.isma.compiler.hsm.core.models.IsmaSyntaxError
import ru.nstu.isma.compiler.hsm.jvm.AnalyzedHybridSystemClassBuilder
import ru.nstu.isma.compiler.hsm.jvm.utils.MemoryFileManager
import ru.nstu.isma.compiler.hsm.jvm.utils.MemoryJavaFileObject
import ru.nstu.isma.lisma.LismaTranslator
import ru.nstu.isma.compiler.hsm.fdm.FDMConverter
import java.io.File
import javax.tools.DiagnosticListener
import javax.tools.ToolProvider
import kotlin.system.exitProcess

/**
 * Standalone LISMA compiler CLI.
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

    val errors = IsmaErrorList()
    val model = LismaTranslator().translate(source, errors)

    if (validate) {
        if (errors.isEmpty()) return
        printlnErrorLines(errors)
        exitProcess(1)
    }

    if (errors.isNotEmpty()) {
        printlnErrorLines(errors)
        exitProcess(1)
    }

    val processed: HSM = if (model.isPDE) {
        FDMConverter(model).convert() ?: fail("FDM conversion failed")
    } else {
        model
    }

    val builder = AnalyzedHybridSystemClassBuilder(processed, PACKAGE_NAME, CLASS_NAME)
    val classSource = builder.buildSourceCode()
    val classBytes = compileToClassBytes(PACKAGE_NAME, CLASS_NAME, classSource)

    if (outPath != null) {
        File(outPath).writeBytes(classBytes)
    } else {
        System.out.write(classBytes)
        System.out.flush()
    }
}

/** Writes one `<row>:<col>:<message>` line per error to stderr. */
private fun printlnErrorLines(errors: IsmaErrorList) {
    errors.forEach { error ->
        val (row, col, message) = when (error) {
            is IsmaSyntaxError -> Triple(error.row, error.col, error.msg)
            is IsmaSemanticError -> Triple(-1, -1, error.msg)
        }
        System.out.println("$row:$col:$message")
    }
    System.out.flush()
}

private fun compileToClassBytes(packageName: String, className: String, sourceCode: String): ByteArray {
    val compiler = ToolProvider.getSystemJavaCompiler()
        ?: error("System JavaCompiler not available")

    val manager = MemoryFileManager(compiler.getStandardFileManager(null, null, null))
    val options = mutableListOf("-classpath", runtimeClasspath())
    val file = MemoryJavaFileObject("$packageName.$className", sourceCode)
    val listener = CliDiagnosticListener()

    val success = compiler.getTask(null, manager, listener, options, null, listOf(file)).call()
    if (!success) {
        fail("javac failed to compile the generated hybrid-system class")
    }

    return manager.compiledClassBytes
        ?: fail("javac produced no class output")
}

/**
 * Builds the classpath for in-process javac. In classpath mode this is
 * [java.class.path]; in module mode that property is empty, so the module
 * path entries are expanded (jars inside directories are listed explicitly)
 * and passed as classpath entries instead.
 */
private fun runtimeClasspath(): String {
    val parts = mutableListOf<String>()
    System.getProperty("java.class.path")?.takeIf { it.isNotBlank() }?.let { parts.add(it) }
    System.getProperty("jdk.module.path")?.takeIf { it.isNotBlank() }
        ?.split(File.pathSeparator)
        ?.forEach { entry ->
            val file = File(entry)
            if (file.isDirectory) {
                file.listFiles { f -> f.name.endsWith(".jar") }?.forEach { parts.add(it.absolutePath) }
            } else {
                parts.add(entry)
            }
        }
    return parts.joinToString(File.pathSeparator)
}

private fun fail(message: String): Nothing {
    printlnError(message)
    exitProcess(1)
}

private fun printlnError(message: String) {
    System.err.println("lisma-compiler-cli: $message")
}

private class CliDiagnosticListener : DiagnosticListener<Any> {
    override fun report(diagnostic: javax.tools.Diagnostic<*>?) {
        if (diagnostic != null) {
            printlnError(diagnostic.toString())
        }
    }
}

private const val PACKAGE_NAME = "ru.nstu.isma.core.simulation.controller"
private const val CLASS_NAME = "AnalyzedHybridSystem"
