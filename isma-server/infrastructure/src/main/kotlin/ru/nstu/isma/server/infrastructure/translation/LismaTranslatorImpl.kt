package ru.nstu.isma.server.infrastructure.translation

import ru.nstu.isma.compiler.hsm.core.HSM
import ru.nstu.isma.compiler.hsm.core.models.IsmaErrorList
import ru.nstu.isma.domain.handlers.runSimulation.ILismaTranslator
import ru.nstu.isma.lisma.InputTranslator
import ru.nstu.isma.next.core.fdm.FDMConverter

class LismaTranslatorImpl(
    private val translator: InputTranslator,
) : ILismaTranslator {

    override fun translate(sourceCode: String): Result<HSM> {
        return try {
            val errors = IsmaErrorList()
            val model = translator.translate(sourceCode, errors)

            if (errors.isNotEmpty()) {
                val errorMessages = errors.joinToString("; ") { it.toString() }
                return Result.failure(IllegalArgumentException("Translation failed: $errorMessages"))
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
}
