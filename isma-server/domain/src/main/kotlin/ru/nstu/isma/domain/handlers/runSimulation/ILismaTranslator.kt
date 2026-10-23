package ru.nstu.isma.domain.handlers.runSimulation

import ru.nstu.isma.compiler.hsm.core.IHSM
import ru.nstu.isma.domain.handlers.compileLisma.CompilationError

interface ILismaTranslator {
    fun translate(sourceCode: String): Result<IHSM>
    fun validate(sourceCode: String): List<CompilationError>
}

class TranslationException(val errors: List<CompilationError>) : Exception("Translation failed")
