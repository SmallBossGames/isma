package ru.isma.next.editor.text.services.contracts

interface ITextEditorService {
    val cutEventHandler : HashSet<() -> Unit>
    val copyEventHandler : HashSet<() -> Unit>
    val pasteEventHandler : HashSet<() -> Unit>

    fun cut()
    fun copy()
    fun paste()
}