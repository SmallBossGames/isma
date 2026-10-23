package ru.nstu.isma.lsp.lsp

import org.antlr.v4.runtime.CharStreams
import ru.nstu.isma.lisma.analysis.gen.LismaLexer

/**
 * LISMA language features exposed over LSP.
 *
 * Currently provides full-document semantic tokens computed with the same
 * ANTLR4 lexer the compiler uses. The token mapping mirrors the previous
 * server-side highlighting handler (keywords, comments, numbers); extending
 * highlighting means adding entries to the [typeFor] mapping and to
 * [LismaTokenType].
 */
class LismaLanguageService {

    fun semanticTokens(text: String): List<SemanticTokenData> {
        val tokens = LismaLexer(CharStreams.fromString(text)).allTokens
        val lineStarts = lineStartOffsets(text)
        val result = ArrayList<SemanticTokenData>()

        for (token in tokens) {
            val type = typeFor(token.type) ?: continue

            val start = token.startIndex
            val end = token.stopIndex + 1
            var position = start
            while (position < end) {
                val line = lineOf(position, lineStarts)
                val lineStart = lineStarts[line]
                val lineEnd = if (line + 1 < lineStarts.size) lineStarts[line + 1] else text.length
                val segmentStart = position - lineStart
                val segmentEnd = minOf(end, lineEnd) - lineStart
                result.add(SemanticTokenData(line, segmentStart, segmentEnd - segmentStart, type))
                position = lineEnd
            }
        }
        return result
    }

    private fun typeFor(lexerTokenType: Int): LismaTokenType? = when (lexerTokenType) {
        LismaLexer.CONST_KEYWORD, LismaLexer.STATE_KEYWORD, LismaLexer.FOR_KEYWORD,
        LismaLexer.IF_KEYWORD, LismaLexer.ELSE_KEYWORD, LismaLexer.FROM_KEYWORD,
        LismaLexer.MACRO_KEYWORD, LismaLexer.SET_KEYWORD -> LismaTokenType.KEYWORD

        LismaLexer.COMMENT, LismaLexer.SL_COMMENT -> LismaTokenType.COMMENT

        LismaLexer.FloatingPointLiteral, LismaLexer.DecimalLiteral -> LismaTokenType.NUMBER

        else -> null
    }

    private fun lineStartOffsets(text: String): List<Int> {
        val starts = ArrayList<Int>(1)
        starts.add(0)
        for (i in text.indices) {
            if (text[i] == '\n') starts.add(i + 1)
        }
        return starts
    }

    private fun lineOf(offset: Int, lineStarts: List<Int>): Int {
        var low = 0
        var high = lineStarts.size - 1
        while (low < high) {
            val mid = (low + high + 1) / 2
            if (lineStarts[mid] <= offset) low = mid else high = mid - 1
        }
        return low
    }
}
