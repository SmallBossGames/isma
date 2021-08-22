package ru.isma.next.app.services.editors

import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.isma.next.editor.blueprint.services.ITextEditorFactory
import ru.isma.next.editor.text.IsmaTextEditor

class TextEditorFactory: ITextEditorFactory, KoinComponent {
    override val editor: IsmaTextEditor
        get() = get()
}