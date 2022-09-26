package ru.isma.next.app.services.project

import javafx.collections.FXCollections
import ru.isma.next.app.models.projects.BlueprintProjectModel
import ru.isma.next.app.models.projects.IProjectModel
import ru.isma.next.app.models.projects.LismaProjectModel

class ProjectService {
    val projects = FXCollections.observableSet<IProjectModel>()

    var activeProject: IProjectModel? = null

    fun createNewBlueprint(name: String = "New statechart") {
        BlueprintProjectModel().apply {
            this.name = name
            addBlueprint(this)
        }
    }

    fun createNew(name: String = "New project"){
        LismaProjectModel().apply {
            this.name = name
            addText(this)
        }
    }

    fun addText(project: LismaProjectModel){
        projects.add(project)
    }

    fun addBlueprint(project: BlueprintProjectModel){
        projects.add(project)
    }

    fun close(project: IProjectModel){
        projects.remove(project)
        project.dispose()
    }

    fun closeAll() {
        val temp = projects.toTypedArray()

        projects.clear()

        temp.forEach { it.dispose() }
    }

    fun getAllProjects() = projects.toTypedArray()
}