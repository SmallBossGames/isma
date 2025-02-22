package ru.isma.next.editor.text.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.isma.next.editor.text.services.contracts.IEditorPlatformService

class EditorPlatformService : IEditorPlatformService {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val cutEventInternal = MutableSharedFlow<Unit>()
    override val cutEvent = cutEventInternal.asSharedFlow()

    private val copyEventInternal = MutableSharedFlow<Unit>()
    override val copyEvent = copyEventInternal.asSharedFlow()

    private val pasteEventInternal = MutableSharedFlow<Unit>()
    override val pasteEvent = pasteEventInternal.asSharedFlow()

    override fun cut() {
        coroutineScope.launch { cutEventInternal.emit(Unit) }
    }

    override fun copy() {
        coroutineScope.launch { copyEventInternal.emit(Unit) }
    }

    override fun paste() {
        coroutineScope.launch { pasteEventInternal.emit(Unit) }
    }
}