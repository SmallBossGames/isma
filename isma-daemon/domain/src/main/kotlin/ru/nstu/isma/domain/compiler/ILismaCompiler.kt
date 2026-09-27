package ru.nstu.isma.domain.compiler

import ru.nstu.isma.domain.handlers.compileLisma.CompilationError

/**
 * The outcome of a LISMA compilation: either the compiled `.class` bytes
 * (on success) or the list of compilation errors (on failure).
 */
data class CompileOutcome(
    val artifact: ByteArray?,
    val errors: List<CompilationError>,
) {
    val isSuccess: Boolean get() = artifact != null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CompileOutcome) return false
        return (artifact == null && other.artifact == null) ||
            (artifact != null && other.artifact != null && artifact.contentEquals(other.artifact)) &&
            errors == other.errors
    }

    override fun hashCode(): Int =
        (artifact?.contentHashCode() ?: 0) * 31 + errors.hashCode()
}

/**
 * Compiles and validates LISMA source by running the standalone LISMA compiler
 * CLI as a separate process. The daemon is a pure orchestrator: all
 * translation, FDM conversion, and `.class` compilation happen inside the CLI.
 */
interface ILismaCompiler {
    /** Compiles LISMA source to a self-contained compiled `.class` file. */
    fun compile(sourceCode: String): CompileOutcome

    /** Validates LISMA source, returning all errors (empty list if valid). */
    fun validate(sourceCode: String): List<CompilationError>
}
