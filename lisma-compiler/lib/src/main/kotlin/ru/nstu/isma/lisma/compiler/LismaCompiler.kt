package ru.nstu.isma.lisma.compiler

import ru.nstu.isma.compiler.hsm.core.HSM
import ru.nstu.isma.compiler.hsm.core.models.IsmaError
import ru.nstu.isma.compiler.hsm.core.models.IsmaErrorList
import ru.nstu.isma.compiler.hsm.core.models.IsmaSemanticError
import ru.nstu.isma.compiler.hsm.core.models.IsmaSyntaxError
import ru.nstu.isma.compiler.hsm.fdm.FDMConverter
import ru.nstu.isma.compiler.hsm.jvm.AnalyzedHybridSystemClassBuilder
import ru.nstu.isma.compiler.hsm.jvm.utils.MemoryFileManager
import ru.nstu.isma.compiler.hsm.jvm.utils.MemoryJavaFileObject
import ru.nstu.isma.lisma.LismaTranslator
import java.io.File
import javax.tools.DiagnosticListener
import javax.tools.ToolProvider

/**
 * A single LISMA source error. [row]/[col] are -1 when the error has no source position.
 */
public data class LismaError(
    public val row: Int,
    public val col: Int,
    public val message: String,
)

/**
 * Result of compiling LISMA source.
 */
public sealed interface LismaCompilationResult {
    /**
     * Compilation succeeded. [classBytes] is the self-contained `.class` file — the
     * analyzed hybrid-system class with the translated HSM inlined as a serialized
     * blob (`__ISMA_HSM__`). [model] is the translated (and, for PDE models,
     * FDM-processed) HSM.
     */
    public class Success(
        public val classBytes: ByteArray,
        public val model: HSM,
    ) : LismaCompilationResult

    /** Compilation failed: [errors] are the source errors. */
    public class Failure(
        public val errors: List<LismaError>,
    ) : LismaCompilationResult
}

/**
 * Minimal public API for compiling LISMA source into a self-contained `.class`
 * file, embeddable in other JVM projects.
 *
 * The compiled class is the analyzed hybrid-system class with the translated HSM
 * inlined as a serialized blob; it is loadable via `ru.nstu.isma.lisma.runtime.LismaModelRuntime`.
 *
 * [compile] returns [LismaCompilationResult.Failure] for source errors and throws
 * [IllegalStateException] for internal compiler failures (FDM conversion, javac).
 * [validate] translates the source only and returns the source errors without
 * building the class.
 */
public object LismaCompiler {

    /**
     * Compiles LISMA source into a self-contained `.class` file.
     *
     * @throws IllegalArgumentException if [source] is blank.
     * @throws IllegalStateException on internal compiler failure (FDM conversion, javac).
     */
    public fun compile(source: String): LismaCompilationResult {
        require(source.isNotBlank()) { "empty input: expected LISMA source" }
        val errors = IsmaErrorList()
        val model = LismaTranslator().translate(source, errors)
        if (errors.isNotEmpty()) {
            return LismaCompilationResult.Failure(errors.map(::toLismaError))
        }
        val processed: HSM = if (model.isPDE) {
            FDMConverter(model).convert() ?: error("FDM conversion failed")
        } else {
            model
        }
        val builder = AnalyzedHybridSystemClassBuilder(processed, GENERATED_PACKAGE_NAME, GENERATED_CLASS_NAME)
        val classSource = builder.buildSourceCode()
        val classBytes = compileToClassBytes(GENERATED_PACKAGE_NAME, GENERATED_CLASS_NAME, classSource)
        return LismaCompilationResult.Success(classBytes, processed)
    }

    /**
     * Translates LISMA source and returns the source errors (empty on success)
     * without building the compiled class.
     *
     * @throws IllegalArgumentException if [source] is blank.
     */
    public fun validate(source: String): List<LismaError> {
        require(source.isNotBlank()) { "empty input: expected LISMA source" }
        val errors = IsmaErrorList()
        LismaTranslator().translate(source, errors)
        return errors.map(::toLismaError)
    }

    private fun toLismaError(error: IsmaError): LismaError = when (error) {
        is IsmaSyntaxError -> LismaError(error.row, error.col, error.msg)
        is IsmaSemanticError -> LismaError(-1, -1, error.msg)
    }

    private fun compileToClassBytes(packageName: String, className: String, sourceCode: String): ByteArray {
        val compiler = ToolProvider.getSystemJavaCompiler()
            ?: error("System JavaCompiler not available")

        val manager = MemoryFileManager(compiler.getStandardFileManager(null, null, null))
        val options = mutableListOf("-classpath", runtimeClasspath())
        val file = MemoryJavaFileObject("$packageName.$className", sourceCode)
        val listener = JavacDiagnosticListener()

        val success = compiler.getTask(null, manager, listener, options, null, listOf(file)).call()
        if (!success) {
            error("javac failed to compile the generated hybrid-system class")
        }

        return manager.compiledClassBytes
            ?: error("javac produced no class output")
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

    private const val GENERATED_PACKAGE_NAME = "ru.nstu.isma.core.simulation.controller"
    private const val GENERATED_CLASS_NAME = "AnalyzedHybridSystem"
}

private class JavacDiagnosticListener : DiagnosticListener<Any> {
    override fun report(diagnostic: javax.tools.Diagnostic<*>?) {
        if (diagnostic != null) {
            System.err.println("lisma-compiler: $diagnostic")
        }
    }
}
