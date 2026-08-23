package ru.isma.next.app.services.project

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.isma.next.app.models.projects.BlueprintProjectModel
import ru.isma.next.app.models.projects.IProjectModel
import ru.isma.next.app.models.projects.LismaProjectModel

class ProjectService : IProjectService {
    private val projectsInternal = mutableListOf<IProjectModel>()

    private val addedProjectsInternal = MutableSharedFlow<IProjectModel>(
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override val projects: List<IProjectModel>
        get() = projectsInternal.toList()

    override val addedProjects: Flow<IProjectModel> = addedProjectsInternal

    override fun createNewBlueprint(name: String) {
        BlueprintProjectModel().apply {
            this.name = name
            addBlueprint(this)
        }
    }

    override fun createNew(name: String) {
        LismaProjectModel().apply {
            this.name = name
            addText(this)
        }
    }

    override fun addText(project: LismaProjectModel) {
        addProject(project)
    }

    override fun addBlueprint(project: BlueprintProjectModel) {
        addProject(project)
    }

    override fun close(project: IProjectModel) {
        projectsInternal.remove(project)
        project.dispose()
    }

    override fun closeAll() {
        val snapshot = projectsInternal.toList()
        projectsInternal.clear()
        snapshot.forEach { it.dispose() }
    }

    private fun addProject(project: IProjectModel) {
        projectsInternal.add(project)
        addedProjectsInternal.tryEmit(project)
    }
}
