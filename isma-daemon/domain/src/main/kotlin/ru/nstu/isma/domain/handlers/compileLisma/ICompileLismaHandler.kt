package ru.nstu.isma.domain.handlers.compileLisma

data class CompilationError(
    val row: Int,
    val column: Int,
    val message: String,
)

data class CompileLismaResult(
    val compiledModelId: String,
    val errors: List<CompilationError>,
    val warnings: List<String>,
)

interface ICompileLismaHandler {
    fun handle(sourceCode: String): CompileLismaResult
}
