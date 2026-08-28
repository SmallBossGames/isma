package ru.isma.next.external.dtos

data class CachedSimulationResult(
    val file: java.io.File,
    val columnNames: List<String>,
)

data class CompileResult(
    val modelId: String,
    val errors: List<CompilationErrorDto>,
    val warnings: List<String>,
)

data class ValidationResult(
    val errors: List<CompilationErrorDto>,
    val warnings: List<String>,
)

data class CompilationErrorDto(
    val row: Int,
    val column: Int,
    val message: String,
)

enum class SyntaxTokenKind {
    UNSPECIFIED,
    KEYWORD,
    COMMENT,
    NUMBER,
    TEXT,
}

data class SyntaxTokenDto(
    val start: Int,
    val length: Int,
    val kind: SyntaxTokenKind,
)
