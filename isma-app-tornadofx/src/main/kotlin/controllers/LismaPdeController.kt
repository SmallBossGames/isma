package controllers

import error.IsmaErrorList
import models.SyntaxErrorModel
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.Token
import ru.nstu.isma.`in`.InputTranslator
import ru.nstu.isma.`in`.lisma.LismaTranslator
import ru.nstu.isma.`in`.lisma.analysis.gen.LismaLexer
import ru.nstu.isma.core.hsm.HSM
import tornadofx.Controller

class LismaPdeController : Controller() {
    private val activeProjectController: ActiveProjectController by inject()
    private val syntaxController: SyntaxErrorController by inject()

    fun translateLisma(): HSM? {
        val project = activeProjectController.activeProject ?: return null

        val errors = IsmaErrorList()
        val translator: InputTranslator = LismaTranslator(project.projectText, errors)
        val translationResult = translator.translate()

        val errorModels = errors.map { x ->
            SyntaxErrorModel(x.row ?: 0, x.col ?: 0, x.msg)
        }

        syntaxController.setErrorList(errorModels)

        return translationResult
    }

    fun getLismaTokens(): List<Token>{
        val project = activeProjectController.activeProject ?: return emptyList<Token>()
        val inputStream = CharStreams.fromString(project.projectText)
        val lexer = LismaLexer(inputStream)
        return lexer.allTokens
    }

    fun getLismaTokens(source: String): List<Token>{
        val project = activeProjectController.activeProject ?: return emptyList<Token>()
        val inputStream = CharStreams.fromString(source)
        val lexer = LismaLexer(inputStream)
        return lexer.allTokens
    }
}