package controllers

import org.fxmisc.richtext.model.StyleSpans
import org.fxmisc.richtext.model.StyleSpansBuilder
import ru.nstu.isma.`in`.lisma.analysis.gen.LismaLexer
import tornadofx.Controller

class SyntaxHighlightingController : Controller() {
    private val lismaPdeController: LismaPdeController by inject()

    fun createHighlightingStyleSpans(source: String): StyleSpans<Collection<String>>? {
        val spansBuilder = StyleSpansBuilder<Collection<String>>()
        var lastKeyword = 0

        lismaPdeController.getLismaTokens(source).forEach {
            when (it.type) {
                LismaLexer.CONST_KEYWORD, LismaLexer.STATE_KEYWORD, LismaLexer.FOR_KEYWORD, LismaLexer.IF_KEYWORD,
                LismaLexer.FROM_KEYWORD -> {
                    spansBuilder
                        .add(listOf("default"),  it.startIndex - lastKeyword)
                        .add(listOf("keyword"), it.stopIndex - it.startIndex + 1)
                    lastKeyword = it.stopIndex + 1
                }
                LismaLexer.COMMENT, LismaLexer.SL_COMMENT -> {
                    spansBuilder
                        .add(listOf("default"),  it.startIndex - lastKeyword)
                        .add(listOf("comment"), it.stopIndex - it.startIndex + 1)
                    lastKeyword = it.stopIndex + 1
                }
                LismaLexer.FloatingPointLiteral, LismaLexer.DecimalLiteral -> {
                    spansBuilder
                        .add(listOf("default"),  it.startIndex - lastKeyword)
                        .add(listOf("decimal"), it.stopIndex - it.startIndex + 1)
                    lastKeyword = it.stopIndex + 1
                }
                else -> {
                    //codeArea.setStyle(it.startIndex, it.stopIndex + 1, listOf("default"))
                }
            }
        }
        spansBuilder.add(listOf("default"),  source.length - lastKeyword)

        return spansBuilder.create()
    }
}