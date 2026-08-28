package ru.isma.next.editor.text.services.contracts

enum class SyntaxTokenKind {
    UNSPECIFIED,
    KEYWORD,
    COMMENT,
    NUMBER,
    TEXT,
}

data class SyntaxToken(
    val start: Int,
    val length: Int,
    val kind: SyntaxTokenKind,
)

interface ISyntaxHighlighter {
    fun highlight(sourceCode: String): List<SyntaxToken>
}
