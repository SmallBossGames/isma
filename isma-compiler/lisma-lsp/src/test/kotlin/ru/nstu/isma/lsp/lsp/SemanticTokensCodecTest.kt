package ru.nstu.isma.lsp.lsp

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SemanticTokensCodecTest {

    @Test
    fun `spec example encodes to spec array`() {
        // Example from the LSP 3.16 spec, "Integer Encoding for Tokens".
        val tokens = listOf(
            SemanticTokenData(2, 5, 3, LismaTokenType.KEYWORD),
            SemanticTokenData(2, 10, 4, LismaTokenType.COMMENT),
            SemanticTokenData(5, 2, 7, LismaTokenType.NUMBER),
        )

        val data = SemanticTokensEncoder.encode(tokens)

        assertEquals(
            listOf(2L, 5L, 3L, 0L, 0L, 0L, 5L, 4L, 1L, 0L, 3L, 2L, 7L, 2L, 0L),
            data,
        )
    }

    @Test
    fun `decode round trips absolute coordinates`() {
        val tokens = listOf(
            SemanticTokenData(0, 0, 5, LismaTokenType.KEYWORD),
            SemanticTokenData(0, 8, 3, LismaTokenType.NUMBER),
            SemanticTokenData(3, 4, 8, LismaTokenType.COMMENT),
            SemanticTokenData(4, 7, 10, LismaTokenType.COMMENT),
            SemanticTokenData(6, 0, 15, LismaTokenType.COMMENT),
        )

        val decoded = SemanticTokensDecoder.decode(SemanticTokensEncoder.encode(tokens))

        assertEquals(
            tokens.map { it.toDecoded() },
            decoded,
        )
    }

    @Test
    fun `empty input encodes to empty array`() {
        assertTrue(SemanticTokensEncoder.encode(emptyList()).isEmpty())
        assertTrue(SemanticTokensDecoder.decode(emptyList()).isEmpty())
    }

    @Test
    fun `unsorted input is sorted before encoding`() {
        val tokens = listOf(
            SemanticTokenData(1, 0, 2, LismaTokenType.NUMBER),
            SemanticTokenData(0, 4, 3, LismaTokenType.KEYWORD),
        )

        val data = SemanticTokensEncoder.encode(tokens)

        assertEquals(
            listOf(0L, 4L, 3L, 0L, 0L, 1L, 0L, 2L, 2L, 0L),
            data,
        )
    }

    private fun SemanticTokenData.toDecoded() =
        DecodedSemanticToken(line, startChar, length, type.ordinal)
}
