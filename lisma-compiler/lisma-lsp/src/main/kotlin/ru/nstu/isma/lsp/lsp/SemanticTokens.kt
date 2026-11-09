package ru.nstu.isma.lsp.lsp

/**
 * Semantic token types advertised in the LSP legend.
 */
enum class LismaTokenType(val legendName: String) {
    KEYWORD("keyword"),
    COMMENT("comment"),
    NUMBER("number"),
}

/** Legend order: index in this list is the wire `tokenType` value. */
val LSP_TOKEN_LEGEND: List<String> = LismaTokenType.entries.map { it.legendName }

/**
 * A semantic token in absolute document coordinates (0-based line and
 * character, character counts in UTF-16-free LISMA source = code points).
 */
data class SemanticTokenData(
    val line: Int,
    val startChar: Int,
    val length: Int,
    val type: LismaTokenType,
)

/**
 * LSP "relative" token format: 5 integers per token
 * `[deltaLine, deltaStart, length, tokenType, tokenModifiers]`.
 *
 * `deltaLine` is cumulative (line += deltaLine); `deltaStart` is relative to
 * the previous token's start when on the same line, absolute otherwise.
 * See LSP 3.16 spec, "Integer Encoding for Tokens".
 */
object SemanticTokensEncoder {

    fun encode(tokens: List<SemanticTokenData>): List<Long> {
        val data = ArrayList<Long>(tokens.size * 5)
        var prevLine = 0
        var prevStart = 0
        for (token in tokens.sortedWith(compareBy({ it.line }, { it.startChar }))) {
            val deltaLine = token.line - prevLine
            val deltaStart = if (deltaLine == 0) token.startChar - prevStart else token.startChar
            data.add(deltaLine.toLong())
            data.add(deltaStart.toLong())
            data.add(token.length.toLong())
            data.add(token.type.ordinal.toLong())
            data.add(0L)
            prevLine = token.line
            prevStart = token.startChar
        }
        return data
    }
}

data class DecodedSemanticToken(
    val line: Int,
    val startChar: Int,
    val length: Int,
    val typeIndex: Int,
)

/**
 * Reference decoder for the [SemanticTokensEncoder] format; UI clients
 * implement the same logic.
 */
object SemanticTokensDecoder {

    fun decode(data: List<Long>): List<DecodedSemanticToken> {
        val result = ArrayList<DecodedSemanticToken>(data.size / 5)
        var line = 0
        var start = 0
        var i = 0
        while (i + 4 < data.size) {
            val deltaLine = data[i]
            val deltaStart = data[i + 1]
            val length = data[i + 2]
            val type = data[i + 3]
            if (deltaLine == 0L) {
                start += deltaStart.toInt()
            } else {
                line += deltaLine.toInt()
                start = deltaStart.toInt()
            }
            result.add(DecodedSemanticToken(line, start, length.toInt(), type.toInt()))
            i += 5
        }
        return result
    }
}
