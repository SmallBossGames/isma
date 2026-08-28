package ru.isma.next.app.services.project

import javafx.scene.Node
import ru.isma.next.app.models.projects.IProjectModel
import ru.isma.next.app.models.projects.ProjectContent

interface ProjectEditorPort {
    fun editorFor(project: IProjectModel): Node

    fun content(project: IProjectModel): ProjectContent

    fun disposeEditor(project: IProjectModel)

    fun disposeAll()
}
