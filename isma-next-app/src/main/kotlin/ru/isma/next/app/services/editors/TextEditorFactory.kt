package ru.isma.next.app.services.editors

import javafx.scene.Node
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.isma.next.editor.blueprint.services.ITextEditorFactory
import ru.isma.next.editor.text.IsmaTextEditor

class TextEditorFactory: ITextEditorFactory, KoinComponent {
    override fun createTextEditor(text: String, onTextChanged: (String) -> Unit): Node {
        return get<IsmaTextEditor>().apply {
            replaceText(text)
            textProperty().addListener { _, _, newText -> onTextChanged(newText) }
        }
    }
}