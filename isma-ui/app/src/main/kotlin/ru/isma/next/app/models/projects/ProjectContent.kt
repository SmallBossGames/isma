package ru.isma.next.app.models.projects

import ru.isma.next.app.models.LismaTextModel
import ru.isma.next.app.services.blueprint.toLismaText
import ru.isma.next.editor.blueprint.models.BlueprintModel

sealed interface ProjectContent {
    val lisma: LismaTextModel

    val fullText: String
        get() = lisma.fullText

    data class Text(override val lisma: LismaTextModel) : ProjectContent

    class Blueprint(
        val model: BlueprintModel,
        override val lisma: LismaTextModel = model.toLismaText(),
    ) : ProjectContent
}
