package ru.isma.next.editor.text.services.contracts

import org.fxmisc.richtext.model.StyleSpans

interface IHighlightingService {
    fun createHighlightingStyleSpans(source: String): StyleSpans<Collection<String>>?
}