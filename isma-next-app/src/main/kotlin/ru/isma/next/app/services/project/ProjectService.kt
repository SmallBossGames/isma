package ru.isma.next.app.services.project

import javafx.collections.FXCollections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import ru.isma.next.app.javafx.addedAsFlow
import ru.isma.next.app.models.projects.BlueprintProjectModel
import ru.isma.next.app.models.projects.IProjectModel
import ru.isma.next.app.models.projects.LismaProjectModel

class ProjectService {
    private val projects = FXCollections.observableSet<IProjectModel>()

    val existedProjects = projects.asFlow()

    val newProjects = projects.addedAsFlow(coroutineScope)

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

    private companion object{
        val coroutineScope = CoroutineScope(Dispatchers.Default)
    }
}