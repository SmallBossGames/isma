package ru.isma.next.editor.text.services

import org.antlr.v4.runtime.CharStreams
import org.fxmisc.richtext.model.StyleSpans
import org.fxmisc.richtext.model.StyleSpansBuilder
import ru.isma.next.editor.text.services.contracts.IHighlightingService
import ru.nstu.isma.lisma.analysis.gen.LismaLexer

class LismaHighlightingService() : IHighlightingService {
    override fun createHighlightingStyleSpans(source: String): StyleSpans<Collection<String>>? {
        val spansBuilder = StyleSpansBuilder<Collection<String>>()
        var lastKeyword = 0

        val inputStream = CharStreams.fromString(source)
        val tokens = LismaLexer(inputStream).allTokens

        tokens.forEach {
            when (it.type) {
                LismaLexer.CONST_KEYWORD, LismaLexer.STATE_KEYWORD, LismaLexer.FOR_KEYWORD, LismaLexer.IF_KEYWORD,
                LismaLexer.FROM_KEYWORD -> {
                    spansBuilder
                        .add(listOf("syntax-default"),  it.startIndex - lastKeyword)
                        .add(listOf("syntax-keyword"), it.stopIndex - it.startIndex + 1)
                    lastKeyword = it.stopIndex + 1
                }
                LismaLexer.COMMENT, LismaLexer.SL_COMMENT -> {
                    spansBuilder
                        .add(listOf("syntax-default"),  it.startIndex - lastKeyword)
                        .add(listOf("syntax-comment"), it.stopIndex - it.startIndex + 1)
                    lastKeyword = it.stopIndex + 1
                }
                LismaLexer.FloatingPointLiteral, LismaLexer.DecimalLiteral -> {
                    spansBuilder
                        .add(listOf("syntax-default"),  it.startIndex - lastKeyword)
                        .add(listOf("syntax-decimal"), it.stopIndex - it.startIndex + 1)
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