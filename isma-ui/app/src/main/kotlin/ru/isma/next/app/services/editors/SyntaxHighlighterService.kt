package ru.isma.next.app.services.editors

import ru.isma.next.editor.text.services.contracts.ISyntaxHighlighter
import ru.isma.next.editor.text.services.contracts.SyntaxToken
import ru.isma.next.editor.text.services.contracts.SyntaxTokenKind
import ru.isma.next.external.SimulationServerFacade
import ru.isma.next.external.dtos.SyntaxTokenKind as ExternalSyntaxTokenKind

class SyntaxHighlighterService(
    private val serverFacade: SimulationServerFacade,
) : ISyntaxHighlighter {

    override fun highlight(sourceCode: String): List<SyntaxToken> {
        return serverFacade.highlightSource(sourceCode).map { dto ->
            SyntaxToken(
                start = dto.start,
                length = dto.length,
                kind = when (dto.kind) {
                    ExternalSyntaxTokenKind.KEYWORD -> SyntaxTokenKind.KEYWORD
                    ExternalSyntaxTokenKind.COMMENT -> SyntaxTokenKind.COMMENT
                    ExternalSyntaxTokenKind.NUMBER -> SyntaxTokenKind.NUMBER
                    else -> SyntaxTokenKind.TEXT
                },
            )
        }
    }
}
