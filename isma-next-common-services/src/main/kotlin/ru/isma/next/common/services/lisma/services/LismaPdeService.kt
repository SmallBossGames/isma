package ru.isma.next.common.services.lisma.services

import error.IsmaErrorList
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.Token
import ru.isma.next.common.services.lisma.FailedTranslation
import ru.isma.next.common.services.lisma.LismaTranslationResult
import ru.isma.next.common.services.lisma.SuccessTranslation
import ru.isma.next.common.services.lisma.models.SyntaxErrorModel
import ru.nstu.isma.`in`.InputTranslator
import ru.nstu.isma.`in`.lisma.LismaTranslator
import ru.nstu.isma.`in`.lisma.analysis.gen.LismaLexer
import ru.nstu.isma.next.core.sim.fdm.FDMNewConverter

class LismaPdeService {
    fun translateLisma(source: String): LismaTranslationResult {
        val errors = IsmaErrorList()
        val translator: InputTranslator = LismaTranslator(source, errors)
        val model = translator.translate()

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