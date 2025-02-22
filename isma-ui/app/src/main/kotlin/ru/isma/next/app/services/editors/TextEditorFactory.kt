package ru.isma.next.app.services.editors

import javafx.scene.Node
import ru.isma.next.editor.blueprint.services.ITextEditorFactory
import ru.isma.next.editor.text.IsmaTextEditor

class TextEditorFactory(private val createInstance: () -> IsmaTextEditor): ITextEditorFactory {
    override fun createTextEditor(text: String, onTextChanged: (String) -> Unit): Node {
        return createInstance().apply {
            replaceText(text)
            textProperty().addListener { _, _, newText -> onTextChanged(newText) }
        }
    }

    override fun disposeInstance(node: Node) {
        (node as IsmaTextEditor).dispose()
    }
}