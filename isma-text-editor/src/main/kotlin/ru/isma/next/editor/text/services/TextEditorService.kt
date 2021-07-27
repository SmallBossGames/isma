package ru.isma.next.editor.text.services

import ru.isma.next.editor.text.events.CopyTextInCurrentEditorRequest
import ru.isma.next.editor.text.events.CutTextInCurrentEditorRequest
import ru.isma.next.editor.text.events.PasteTextInCurrentEditorRequest
import ru.isma.next.editor.text.services.contracts.ITextEditorService
import tornadofx.FX

class TextEditorService : ITextEditorService {
    override fun cut() = FX.eventbus.fire(CutTextInCurrentEditorRequest)
    override fun copy() = FX.eventbus.fire(CopyTextInCurrentEditorRequest)
    override fun paste() = FX.eventbus.fire(PasteTextInCurrentEditorRequest)
}