package controllers

import events.CopyTextInCurrentEditorEvent
import events.CutTextInCurrentEditorEvent
import events.PasteTextInCurrentEditorEvent
import tornadofx.Controller

class TextEditorController : Controller() {
    fun cut() = fire(CutTextInCurrentEditorEvent())
    fun copy() = fire(CopyTextInCurrentEditorEvent())
    fun paste() = fire(PasteTextInCurrentEditorEvent())
}