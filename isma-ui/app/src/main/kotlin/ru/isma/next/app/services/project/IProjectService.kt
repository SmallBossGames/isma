package ru.isma.next.app.services.project

import kotlinx.coroutines.flow.Flow
import ru.isma.next.app.models.projects.BlueprintProjectModel
import ru.isma.next.app.models.projects.IProjectModel
import ru.isma.next.app.models.projects.LismaProjectModel

interface IProjectService {

    val projects: List<IProjectModel>

    val addedProjects: Flow<IProjectModel>

    fun createNewBlueprint(name: String = "New statechart")

    fun createNew(name: String = "New project")

    fun addText(project: LismaProjectModel)

    fun addBlueprint(project: BlueprintProjectModel)

    fun close(project: IProjectModel)

    fun closeAll()
}
