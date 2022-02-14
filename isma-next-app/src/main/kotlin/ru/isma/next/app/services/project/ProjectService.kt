package ru.isma.next.app.services.project

import javafx.collections.FXCollections
import javafx.collections.ObservableSet
import ru.isma.next.app.models.projects.BlueprintProjectModel
import ru.isma.next.app.models.projects.IProjectModel
import ru.isma.next.app.models.projects.LismaProjectModel

class ProjectService {
    val projects = FXCollections.observableSet<IProjectModel>()!!

    var activeProject: IProjectModel? = null

    fun createNewBlueprint(name: String = "New blueprint") {
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
    }

    fun closeAll() {
        projects.clear()
    }

    fun getAllProjects() = projects.toTypedArray()
}