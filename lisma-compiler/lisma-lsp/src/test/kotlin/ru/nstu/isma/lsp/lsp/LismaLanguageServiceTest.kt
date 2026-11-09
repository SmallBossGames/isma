package ru.nstu.isma.lsp.lsp

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class LismaLanguageServiceTest {

    private val service = LismaLanguageService()

    @Test
    fun `empty source yields no tokens`() {
        assertTrue(service.semanticTokens("").isEmpty())
    }

    @Test
    fun `keyword and number are tokenized with offsets`() {
        val tokens = service.semanticTokens("const a = 1;")

        assertEquals(
            listOf(
                SemanticTokenData(0, 0, 5, LismaTokenType.KEYWORD),
                SemanticTokenData(0, 10, 1, LismaTokenType.NUMBER),
            ),
            tokens,
        )
    }

    @Test
    fun `all eight keywords are recognized`() {
        val source = "const state for if else from macro set"
        val tokens = service.semanticTokens(source)

        assertEquals(8, tokens.count { it.type == LismaTokenType.KEYWORD })
    }

    @Test
    fun `identifiers and operators are not tokenized`() {
        val tokens = service.semanticTokens("x' = -a * x;")

        assertTrue(tokens.isEmpty())
    }

    @Test
    fun `block comment is split per line`() {
        val tokens = service.semanticTokens(
            "a = 1;\n/* first\nsecond */\nb = 2;",
        )
        val comments = tokens.filter { it.type == LismaTokenType.COMMENT }
        assertEquals(
            listOf(
                SemanticTokenData(1, 0, 9, LismaTokenType.COMMENT),
                SemanticTokenData(2, 0, 9, LismaTokenType.COMMENT),
            ),
            comments,
        )
    }

    @Test
    fun `line comment spans to end of line`() {
        val tokens = service.semanticTokens("x = 1; // note")

        assertEquals(
            listOf(
                SemanticTokenData(0, 4, 1, LismaTokenType.NUMBER),
                SemanticTokenData(0, 7, 7, LismaTokenType.COMMENT),
            ),
            tokens,
        )
    }

    @Test
    fun `floating point and decimal literals are numbers`() {
        val tokens = service.semanticTokens("a = 1.5; b = .25; c = 42;")

        assertEquals(
            listOf(
                SemanticTokenData(0, 4, 3, LismaTokenType.NUMBER),
                SemanticTokenData(0, 13, 3, LismaTokenType.NUMBER),
                SemanticTokenData(0, 22, 2, LismaTokenType.NUMBER),
            ),
            tokens,
        )
    }
}
