package ru.nstu.isma.domain.handlers.runSimulation

import ru.nstu.isma.compiler.hsm.core.HSM
import ru.nstu.isma.compiler.hsm.core.models.IsmaErrorList

interface ILismaTranslator {
    fun translate(sourceCode: String): Result<HSM>
    fun validate(sourceCode: String): IsmaErrorList
}

class TranslationException(val errors: IsmaErrorList) : Exception("Translation failed")
