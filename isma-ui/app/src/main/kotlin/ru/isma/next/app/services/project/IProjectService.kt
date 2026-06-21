package ru.isma.next.app.services.project

import javafx.collections.ObservableSet
import ru.isma.next.app.models.projects.BlueprintProjectModel
import ru.isma.next.app.models.projects.IProjectModel
import ru.isma.next.app.models.projects.LismaProjectModel

interface IProjectService {

    val projects: ObservableSet<IProjectModel>

    var activeProject: IProjectModel?

    fun createNewBlueprint(name: String = "New statechart")

    fun createNew(name: String = "New project")

    fun addText(project: LismaProjectModel)

    fun addBlueprint(project: BlueprintProjectModel)

    fun close(project: IProjectModel)

    fun closeAll()

    fun getAllProjects(): Array<IProjectModel>
}
