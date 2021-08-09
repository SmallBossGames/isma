package ru.isma.next.common.services.lisma.services

import ru.nstu.isma.core.hsm.models.IsmaErrorList
import ru.isma.next.common.services.lisma.FailedTranslation
import ru.isma.next.common.services.lisma.LismaTranslationResult
import ru.isma.next.common.services.lisma.SuccessTranslation
import ru.nstu.isma.lisma.InputTranslator
import ru.nstu.isma.next.core.sim.fdm.FDMNewConverter

class LismaPdeService(
    private val translator: InputTranslator
) {
    fun translateLisma(source: String): LismaTranslationResult {
        val errors = IsmaErrorList()
        val model = translator.translate(source, errors)

        val processedModel = if (model.isPDE) model else FDMNewConverter(model).convert()

        if (processedModel != null && errors.isEmpty()) {
            return SuccessTranslation(processedModel)
        }

        return FailedTranslation(errors)
    }
}