package ru.nstu.isma.domain.handlers.validateLisma

import ru.nstu.isma.compiler.hsm.core.models.IsmaSemanticError
import ru.nstu.isma.compiler.hsm.core.models.IsmaSyntaxError
import ru.nstu.isma.domain.handlers.compileLisma.CompilationError
import ru.nstu.isma.domain.handlers.runSimulation.ILismaTranslator
import ru.nstu.isma.domain.handlers.runSimulation.TranslationException

class ValidateLismaHandlerImpl(
    private val translator: ILismaTranslator,
) : IValidateLismaHandler {

    override fun handle(sourceCode: String): ValidateLismaResult {
        val ismaErrors = translator.validate(sourceCode)

        if (ismaErrors.isEmpty()) {
            return ValidateLismaResult(
                errors = emptyList(),
                warnings = emptyList(),
            )
        }

        val compilationErrors = ismaErrors.map { ismaError ->
            when (ismaError) {
                is IsmaSyntaxError -> CompilationError(ismaError.row, ismaError.col, ismaError.msg)
                is IsmaSemanticError -> CompilationError(-1, -1, ismaError.msg)
            }
        }

        return ValidateLismaResult(
            errors = compilationErrors,
            warnings = emptyList(),
        )
    }
}
