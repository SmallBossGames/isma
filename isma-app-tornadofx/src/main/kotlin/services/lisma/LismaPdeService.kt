package services.lisma

import error.IsmaErrorList
import models.FailedTranslation
import models.LismaTranslationResult
import models.SuccessTranslation
import models.SyntaxErrorModel
import models.projects.IProjectModel
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.Token
import ru.nstu.isma.`in`.InputTranslator
import ru.nstu.isma.`in`.lisma.LismaTranslator
import ru.nstu.isma.`in`.lisma.analysis.gen.LismaLexer
import ru.nstu.isma.core.hsm.HSM
import ru.nstu.isma.next.core.sim.fdm.FDMNewConverter
import services.ModelErrorService
import services.project.ProjectService

class LismaPdeService() {
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

    fun getLismaTokens(source: String): List<Token> {
        val inputStream = CharStreams.fromString(source)
        val lexer = LismaLexer(inputStream)
        return lexer.allTokens
    }
}