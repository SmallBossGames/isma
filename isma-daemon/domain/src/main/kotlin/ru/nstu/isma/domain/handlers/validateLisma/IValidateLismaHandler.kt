package ru.nstu.isma.domain.handlers.validateLisma

import ru.nstu.isma.domain.handlers.compileLisma.CompilationError

data class ValidateLismaResult(
    val errors: List<CompilationError>,
    val warnings: List<String>,
)

interface IValidateLismaHandler {
    fun handle(sourceCode: String): ValidateLismaResult
}
