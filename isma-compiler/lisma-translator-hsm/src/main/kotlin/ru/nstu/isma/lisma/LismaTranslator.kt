package ru.nstu.isma.lisma

import ru.nstu.isma.core.hsm.models.IsmaErrorList
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import ru.nstu.isma.core.hsm.HSM
import ru.nstu.isma.lisma.translator.ASTTreeTranslator
import ru.nstu.isma.lisma.analysis.gen.LismaLexer
import ru.nstu.isma.lisma.analysis.gen.LismaParser
import ru.nstu.isma.lisma.parser.LismaErrorListener

/**
 * Created by Bessonov Alex
 * Date: 05.12.13
 * Time: 13:11
 */
class LismaTranslator : InputTranslator {
    override fun translate(text: String, errors: IsmaErrorList): HSM {
        //макросы
        // объект входной текстовой модели
        val cs = CharStreams.fromString(text)
        // лексер
        val lexer = LismaLexer(cs)
        // разбираем в поток токенов
        val tokens = CommonTokenStream(lexer)
        // парсим в синтаксическое дерево
        val parser = LismaParser(tokens)
        val errorListener = LismaErrorListener(errors)
        parser.addErrorListener(errorListener)
        val tree = parser.lisma()
        // преобразователь из дерева в объектную модель HMS
        val ast2HSM = ASTTreeTranslator(tree, errors)
        return ast2HSM.parse()
    }
}