package ru.isma.next.editor.blueprint.services

import javafx.scene.Node

interface ITextEditorFactory {
    fun createTextEditor(text: String, onTextChanged: (String) -> Unit): Node
}