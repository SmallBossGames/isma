package ru.isma.next.editor.text.services

import org.fxmisc.richtext.model.StyleSpans
import org.fxmisc.richtext.model.StyleSpansBuilder
import ru.isma.next.editor.text.services.contracts.IHighlightingService
import ru.isma.next.editor.text.services.contracts.ISyntaxHighlighter
import ru.isma.next.editor.text.services.contracts.SyntaxTokenKind

class RemoteLismaHighlightingService(
    private val syntaxHighlighter: ISyntaxHighlighter,
) : IHighlightingService {

    override fun createHighlightingStyleSpans(source: String): StyleSpans<Collection<String>>? {
        val spansBuilder = StyleSpansBuilder<Collection<String>>()
        var lastEnd = 0

        val tokens = syntaxHighlighter.highlight(source)

        tokens.sortedBy { it.start }.forEach { token ->
            val tokenStart = token.start
            val tokenEnd = token.start + token.length

            if (tokenStart > lastEnd) {
                spansBuilder.add(listOf("syntax-default"), tokenStart - lastEnd)
            }

            val styleClass = when (token.kind) {
                SyntaxTokenKind.KEYWORD -> "syntax-keyword"
                SyntaxTokenKind.COMMENT -> "syntax-comment"
                SyntaxTokenKind.NUMBER -> "syntax-decimal"
                else -> "syntax-default"
            }

            spansBuilder.add(listOf(styleClass), token.length)
            lastEnd = tokenEnd
        }

        if (lastEnd < source.length) {
            spansBuilder.add(listOf("syntax-default"), source.length - lastEnd)
        }

        return spansBuilder.create()
    }
}
