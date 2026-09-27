package ru.nstu.isma.domain.handlers.validateLisma

import ru.nstu.isma.domain.compiler.ILismaCompiler

class ValidateLismaHandlerImpl(
    private val compiler: ILismaCompiler,
) : IValidateLismaHandler {
    override fun handle(sourceCode: String): ValidateLismaResult {
        return ValidateLismaResult(
            errors = compiler.validate(sourceCode),
            warnings = emptyList(),
        )
    }
}
