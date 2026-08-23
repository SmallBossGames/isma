package ru.isma.next.app.viewmodels

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Node
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import ru.isma.next.app.models.projects.IProjectModel
import ru.isma.next.app.services.project.IProjectService
import ru.isma.next.app.services.project.ProjectEditorPort

class ProjectItemViewModel(
    val model: IProjectModel,
    private val editorPort: ProjectEditorPort,
) {
    val nameProperty = SimpleStringProperty(model.name)

    val editorNode: Node by lazy { editorPort.editorFor(model) }

    fun refreshName() {
        nameProperty.value = model.name
    }
}

class EditorTabPaneViewModel(
    private val projectService: IProjectService,
    private val editorPort: ProjectEditorPort,
    private val dispatcher: CoroutineDispatcher = Dispatchers.JavaFx,
) : AutoCloseable {
    val projects: ObservableList<ProjectItemViewModel> = FXCollections.observableArrayList()

    var activeProject: ProjectItemViewModel? = null
        private set

    private val scope = CoroutineScope(dispatcher + SupervisorJob())

    init {
        scope.launch {
            // Snapshot runs in the same coroutine right before subscription, with no
            // suspension point in between, so no project addition can be missed.
            projectService.projects.forEach { addProjectView(it) }
            projectService.addedProjects.collect { addProjectView(it) }
        }
    }

    private fun addProjectView(project: IProjectModel) {
        if (projects.any { it.model == project }) {
            return
        }
        projects.add(ProjectItemViewModel(project, editorPort))
    }

    fun selectProject(item: ProjectItemViewModel?) {
        activeProject = item
    }

    fun closeProject(item: ProjectItemViewModel) {
        editorPort.disposeEditor(item.model)
        projectService.close(item.model)
        projects.remove(item)

        if (activeProject == item) {
            activeProject = projects.lastOrNull()
        }
    }

    fun closeAllProjects() {
        editorPort.disposeAll()
        projectService.closeAll()
        projects.clear()
        activeProject = null
    }

    fun refreshName(model: IProjectModel) {
        projects.firstOrNull { it.model == model }?.refreshName()
    }

    override fun close() {
        scope.cancel()
    }
}
