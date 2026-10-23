package ru.nstu.isma.server.infrastructure.translation

import ru.nstu.isma.compiler.hsm.core.IHSM
import ru.nstu.isma.compiler.hsm.core.models.IsmaError
import ru.nstu.isma.compiler.hsm.core.models.IsmaErrorList
import ru.nstu.isma.compiler.hsm.core.models.IsmaSemanticError
import ru.nstu.isma.compiler.hsm.core.models.IsmaSyntaxError
import ru.nstu.isma.domain.handlers.compileLisma.CompilationError
import ru.nstu.isma.domain.handlers.runSimulation.ILismaTranslator
import ru.nstu.isma.domain.handlers.runSimulation.TranslationException
import ru.nstu.isma.lisma.InputTranslator
import ru.nstu.isma.next.core.fdm.FDMConverter

class LismaTranslatorImpl(
    private val translator: InputTranslator,
) : ILismaTranslator {

    override fun translate(sourceCode: String): Result<IHSM> {
        return try {
            val errors = IsmaErrorList()
            val model = translator.translate(sourceCode, errors)

            if (errors.isNotEmpty()) {
                return Result.failure(TranslationException(errors.map { toCompilationError(it) }))
            }

            val processedModel = if (model.isPDE) {
                FDMConverter(model).convert()
                    ?: return Result.failure(IllegalArgumentException("FDM conversion failed"))
            } else {
                model
            }

            Result.success(processedModel)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun validate(sourceCode: String): List<CompilationError> {
        val errors = IsmaErrorList()
        translator.translate(sourceCode, errors)
        return errors.map { toCompilationError(it) }
    }

    private fun toCompilationError(error: IsmaError): CompilationError = when (error) {
        is IsmaSyntaxError -> CompilationError(error.row, error.col, error.msg)
        is IsmaSemanticError -> CompilationError(-1, -1, error.msg)
    }
}
