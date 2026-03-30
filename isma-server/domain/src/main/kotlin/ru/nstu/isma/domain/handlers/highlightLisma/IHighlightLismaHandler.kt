package ru.nstu.isma.domain.handlers.highlightLisma

enum class SyntaxKind {
    KEYWORD,
    COMMENT,
    NUMBER,
    TEXT,
}

data class SyntaxToken(
    val start: Int,
    val length: Int,
    val kind: SyntaxKind,
)

data class HighlightLismaResult(
    val tokens: List<SyntaxToken>,
)

interface IHighlightLismaHandler {
    fun handle(sourceCode: String): HighlightLismaResult
}
