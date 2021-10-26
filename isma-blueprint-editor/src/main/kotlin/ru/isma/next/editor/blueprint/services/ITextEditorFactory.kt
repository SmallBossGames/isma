package ru.isma.next.editor.blueprint.services

import ru.isma.next.editor.text.IsmaTextEditor

interface ITextEditorFactory {
    fun createTextEditor(): IsmaTextEditor
}