package ru.isma.next.editor.text.services

import ru.isma.next.editor.text.services.contracts.ITextEditorService

class TextEditorService : ITextEditorService {
    override val cutEventHandler = HashSet<() -> Unit>()
    override val copyEventHandler = HashSet<() -> Unit>()
    override val pasteEventHandler = HashSet<() -> Unit>()

    override fun cut() = cutEventHandler.forEach { it() }
    override fun copy() = copyEventHandler.forEach { it() }
    override fun paste() = pasteEventHandler.forEach { it() }
}