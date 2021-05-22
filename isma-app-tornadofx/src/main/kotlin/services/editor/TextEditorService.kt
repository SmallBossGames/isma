package services.editor

import events.CopyTextInCurrentEditorEvent
import events.CutTextInCurrentEditorEvent
import events.PasteTextInCurrentEditorEvent
import tornadofx.Controller
import tornadofx.FX

class TextEditorService {
    fun cut() = FX.eventbus.fire(CutTextInCurrentEditorEvent())
    fun copy() = FX.eventbus.fire(CopyTextInCurrentEditorEvent())
    fun paste() = FX.eventbus.fire(PasteTextInCurrentEditorEvent())
}