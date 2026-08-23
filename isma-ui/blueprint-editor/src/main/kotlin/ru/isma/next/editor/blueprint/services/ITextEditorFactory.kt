package ru.isma.next.editor.blueprint.services

import javafx.beans.property.Property
import javafx.scene.Node

interface ITextEditor {
    val node: Node

    val text: Property<String>

    fun dispose()
}

interface ITextEditorFactory {
    fun createEditor(): ITextEditor
}
