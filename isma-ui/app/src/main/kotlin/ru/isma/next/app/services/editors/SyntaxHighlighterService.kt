package ru.isma.next.app.services.editors

import ru.isma.next.editor.text.services.contracts.ISyntaxHighlighter
import ru.isma.next.editor.text.services.contracts.SyntaxToken
import ru.isma.next.editor.text.services.contracts.SyntaxTokenKind
import ru.isma.next.external.SimulationServerFacade

class SyntaxHighlighterService(
    private val serverFacade: SimulationServerFacade,
) : ISyntaxHighlighter {

    override fun highlight(sourceCode: String): List<SyntaxToken> {
        return serverFacade.highlightSource(sourceCode).map { dto ->
            SyntaxToken(
                start = dto.start,
                length = dto.length,
                kind = when (dto.kind) {
                    ru.isma.next.external.SyntaxTokenKind.KEYWORD -> SyntaxTokenKind.KEYWORD
                    ru.isma.next.external.SyntaxTokenKind.COMMENT -> SyntaxTokenKind.COMMENT
                    ru.isma.next.external.SyntaxTokenKind.NUMBER -> SyntaxTokenKind.NUMBER
                    else -> SyntaxTokenKind.TEXT
                },
            )
        }
    }
}
