package ru.nstu.isma.domain.handlers.validateLisma

import ru.nstu.isma.domain.handlers.runSimulation.ILismaTranslator

class ValidateLismaHandlerImpl(
    private val translator: ILismaTranslator,
) : IValidateLismaHandler {
    override fun handle(sourceCode: String): ValidateLismaResult {
        return ValidateLismaResult(
            errors = translator.validate(sourceCode),
            warnings = emptyList(),
        )
    }
}
