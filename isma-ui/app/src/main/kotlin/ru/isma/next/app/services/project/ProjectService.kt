package ru.isma.next.app.services.project

import javafx.collections.FXCollections
import ru.isma.next.app.models.projects.BlueprintProjectModel
import ru.isma.next.app.models.projects.IProjectModel
import ru.isma.next.app.models.projects.LismaProjectModel

class ProjectService : IProjectService {
    override val projects = FXCollections.observableSet<IProjectModel>()

    override var activeProject: IProjectModel? = null

    override fun createNewBlueprint(name: String) {
        BlueprintProjectModel().apply {
            this.name = name
            addBlueprint(this)
        }
    }

    override fun createNew(name: String){
        LismaProjectModel().apply {
            this.name = name
            addText(this)
        }
    }

    override fun addText(project: LismaProjectModel){
        projects.add(project)
    }

    override fun addBlueprint(project: BlueprintProjectModel){
        projects.add(project)
    }

    override fun close(project: IProjectModel){
        projects.remove(project)
        project.dispose()
    }

    override fun closeAll() {
        val temp = projects.toTypedArray()

        projects.clear()

        temp.forEach { it.dispose() }
    }

    override fun getAllProjects() = projects.toTypedArray()
}
