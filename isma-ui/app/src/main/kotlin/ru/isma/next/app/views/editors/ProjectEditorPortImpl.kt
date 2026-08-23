package ru.isma.next.app.views.editors

import javafx.scene.Node
import ru.isma.next.app.models.projects.BlueprintProjectModel
import ru.isma.next.app.models.projects.IProjectModel
import ru.isma.next.app.models.projects.LismaProjectModel
import ru.isma.next.app.models.projects.ProjectContent
import ru.isma.next.app.services.editors.TextEditorFactory
import ru.isma.next.app.services.project.ProjectEditorPort
import ru.isma.next.editor.blueprint.views.IsmaBlueprintEditor
import ru.isma.next.editor.text.IsmaTextEditor
import ru.isma.next.editor.text.services.contracts.IEditorPlatformService
import ru.isma.next.editor.text.services.contracts.IHighlightingService

class ProjectEditorPortImpl(
    private val editorPlatformService: IEditorPlatformService,
    private val highlightingService: IHighlightingService,
) : ProjectEditorPort {
    private val editors = mutableMapOf<IProjectModel, Node>()

    private val blueprintTextEditorFactory = TextEditorFactory {
        IsmaTextEditor(editorPlatformService, highlightingService)
    }

    override fun editorFor(project: IProjectModel): Node = editors.getOrPut(project) {
        createEditor(project)
    }

    override fun content(project: IProjectModel): ProjectContent = when (project) {
        is LismaProjectModel -> ProjectContent.Text(
            (editorFor(project) as IsmaTextEditor).textProperty().value
        )
        is BlueprintProjectModel -> ProjectContent.Blueprint(
            (editorFor(project) as IsmaBlueprintEditor).getBlueprintModel()
        )
        else -> throw IllegalArgumentException("Unsupported project type: ${project::class}")
    }

    private fun createEditor(project: IProjectModel): Node = when (project) {
        is LismaProjectModel -> IsmaTextEditor(editorPlatformService, highlightingService).apply {
            replaceText(project.lismaText)
        }
        is BlueprintProjectModel -> IsmaBlueprintEditor(blueprintTextEditorFactory).apply {
            setBlueprintModel(project.blueprint)
        }
        else -> throw IllegalArgumentException("Unsupported project type: ${project::class}")
    }

    override fun disposeEditor(project: IProjectModel) {
        val editor = editors.remove(project) ?: return

        when (editor) {
            is IsmaTextEditor -> editor.dispose()
            is IsmaBlueprintEditor -> editor.dispose()
            else -> {}
        }
    }

    override fun disposeAll() {
        editors.keys.toList().forEach { disposeEditor(it) }
    }
}
