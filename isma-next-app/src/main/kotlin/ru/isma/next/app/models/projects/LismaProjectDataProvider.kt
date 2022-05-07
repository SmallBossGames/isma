package ru.isma.next.app.models.projects

import ru.isma.next.editor.text.IsmaTextEditor

class LismaProjectDataProvider(private val ismaTextEditor: IsmaTextEditor) {
    var text: String
        get() = ismaTextEditor.text
        set(value) = ismaTextEditor.replaceText(value)
}