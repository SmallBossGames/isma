package controllers

import error.IsmaErrorList
import models.SyntaxErrorModel
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.Token
import ru.nstu.isma.`in`.InputTranslator
import ru.nstu.isma.`in`.lisma.LismaTranslator
import ru.nstu.isma.`in`.lisma.analysis.gen.LismaLexer
import ru.nstu.isma.core.hsm.HSM
import ru.nstu.isma.next.core.sim.fdm.FDMNewConverter
import tornadofx.Controller

class LismaPdeController : Controller() {
    private val activeProjectController: ActiveProjectController by inject()
    private val syntaxController: SyntaxErrorController by inject()

    fun translateLisma(): HSM? {
        val project = activeProjectController.activeProject ?: return null

        val errors = IsmaErrorList()
        val translator: InputTranslator = LismaTranslator(project.projectText, errors)
        val model = translator.translate()

        val processedModel = if (model.isPDE) model else FDMNewConverter(model).convert()

        val errorModels = errors.map {
            SyntaxErrorModel(it.row ?: 0, it.col ?: 0, it.msg)
        }

        syntaxController.setErrorList(errorModels)

        return processedModel
    }

    fun getLismaTokens(): List<Token>{
        val project = activeProjectController.activeProject ?: return emptyList<Token>()
        val inputStream = CharStreams.fromString(project.projectText)
        val lexer = LismaLexer(inputStream)
        return lexer.allTokens
    }

    fun getLismaTokens(source: String): List<Token> {
        val inputStream = CharStreams.fromString(source)
        val lexer = LismaLexer(inputStream)
        return lexer.allTokens
    }
}