package services

import error.IsmaErrorList
import models.SyntaxErrorModel
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.Token
import ru.nstu.isma.`in`.InputTranslator
import ru.nstu.isma.`in`.lisma.LismaTranslator
import ru.nstu.isma.`in`.lisma.analysis.gen.LismaLexer
import ru.nstu.isma.core.hsm.HSM
import ru.nstu.isma.next.core.sim.fdm.FDMNewConverter

class LismaPdeService(private val projectService: ProjectService, private val syntaxService: SyntaxErrorService) {

    fun translateLisma(): HSM? {
        val project = projectService.activeProject ?: return null

        val errors = IsmaErrorList()
        val translator: InputTranslator = LismaTranslator(project.lismaText, errors)
        val model = translator.translate()

        val processedModel = if (model.isPDE) model else FDMNewConverter(model).convert()

        val errorModels = errors.map {
            SyntaxErrorModel(it.row ?: 0, it.col ?: 0, it.msg)
        }

        syntaxService.setErrorList(errorModels)

        return processedModel
    }

    fun getLismaTokens(): List<Token>{
        val project = projectService.activeProject ?: return emptyList<Token>()
        val inputStream = CharStreams.fromString(project.lismaText)
        val lexer = LismaLexer(inputStream)
        return lexer.allTokens
    }

    fun getLismaTokens(source: String): List<Token> {
        val inputStream = CharStreams.fromString(source)
        val lexer = LismaLexer(inputStream)
        return lexer.allTokens
    }
}