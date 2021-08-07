package ru.isma.next.common.services.lisma.services

import error.IsmaErrorList
import ru.isma.next.common.services.lisma.FailedTranslation
import ru.isma.next.common.services.lisma.LismaTranslationResult
import ru.isma.next.common.services.lisma.SuccessTranslation
import ru.isma.next.common.services.lisma.models.SyntaxErrorModel
import ru.nstu.isma.lisma.InputTranslator
import ru.nstu.isma.next.core.sim.fdm.FDMNewConverter

class LismaPdeService(
    private val translator: InputTranslator
) {
    fun translateLisma(source: String): LismaTranslationResult {
        val errors = IsmaErrorList()
        val model = translator.translate(source, errors)

        val processedModel = if (model.isPDE) model else FDMNewConverter(model).convert()

        val errorModels = errors.map {
            SyntaxErrorModel(it.row ?: 0, it.col ?: 0, it.msg)
        }

        if (processedModel != null && errorModels.isEmpty()) {
            return SuccessTranslation(processedModel)
        }

        return FailedTranslation(errorModels)
    }
}