package ru.isma.next.editor.text.services.contracts

import kotlinx.coroutines.flow.SharedFlow

interface IEditorPlatformService {
    val cutEvent: SharedFlow<Unit>
    val copyEvent: SharedFlow<Unit>
    val pasteEvent: SharedFlow<Unit>

    fun cut()
    fun copy()
    fun paste()
}