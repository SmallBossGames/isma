package ru.isma.next.app.services.editors

import javafx.beans.property.Property
import javafx.scene.Node
import ru.isma.next.editor.blueprint.services.ITextEditor
import ru.isma.next.editor.blueprint.services.ITextEditorFactory
import ru.isma.next.editor.text.IsmaTextEditor
import ru.isma.next.editor.text.services.contracts.IEditorPlatformService
import ru.isma.next.editor.text.services.contracts.IHighlightingService

class TextEditorFactory(
    private val editorPlatformService: IEditorPlatformService,
    private val highlightingService: IHighlightingService,
) : ITextEditorFactory {

    override fun createEditor(): ITextEditor =
        TextEditorAdapter(IsmaTextEditor(editorPlatformService, highlightingService))

    private class TextEditorAdapter(private val editor: IsmaTextEditor) : ITextEditor {
        override val node: Node = editor

        override val text: Property<String> = editor.textProperty()

        override fun dispose() {
            editor.dispose()
        }
    }
}
