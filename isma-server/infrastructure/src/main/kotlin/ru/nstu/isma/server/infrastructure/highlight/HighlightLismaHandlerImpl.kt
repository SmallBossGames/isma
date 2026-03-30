package ru.nstu.isma.server.infrastructure.highlight

import org.antlr.v4.runtime.CharStreams
import ru.nstu.isma.domain.handlers.highlightLisma.HighlightLismaResult
import ru.nstu.isma.domain.handlers.highlightLisma.IHighlightLismaHandler
import ru.nstu.isma.domain.handlers.highlightLisma.SyntaxKind
import ru.nstu.isma.domain.handlers.highlightLisma.SyntaxToken
import ru.nstu.isma.lisma.analysis.gen.LismaLexer

class HighlightLismaHandlerImpl : IHighlightLismaHandler {

    override fun handle(sourceCode: String): HighlightLismaResult {
        val inputStream = CharStreams.fromString(sourceCode)
        val tokens = LismaLexer(inputStream).allTokens

        val syntaxTokens = tokens.mapNotNull { token ->
            val kind = when (token.type) {
                LismaLexer.CONST_KEYWORD, LismaLexer.STATE_KEYWORD, LismaLexer.FOR_KEYWORD,
                LismaLexer.IF_KEYWORD, LismaLexer.ELSE_KEYWORD, LismaLexer.FROM_KEYWORD,
                LismaLexer.MACRO_KEYWORD, LismaLexer.SET_KEYWORD -> SyntaxKind.KEYWORD

                LismaLexer.COMMENT, LismaLexer.SL_COMMENT -> SyntaxKind.COMMENT

                LismaLexer.FloatingPointLiteral, LismaLexer.DecimalLiteral -> SyntaxKind.NUMBER

                else -> return@mapNotNull null
            }

            SyntaxToken(
                start = token.startIndex,
                length = token.stopIndex - token.startIndex + 1,
                kind = kind,
            )
        }

        return HighlightLismaResult(tokens = syntaxTokens)
    }
}
