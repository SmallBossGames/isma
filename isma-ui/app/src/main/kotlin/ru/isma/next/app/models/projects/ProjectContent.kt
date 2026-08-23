package ru.isma.next.app.models.projects

import ru.isma.next.editor.blueprint.models.BlueprintModel

sealed interface ProjectContent {
    val fullText: String

    data class Text(override val fullText: String) : ProjectContent

    class Blueprint(val model: BlueprintModel) : ProjectContent {
        override val fullText: String = model.toLismaText().fullText
    }
}
