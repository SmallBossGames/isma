package ru.isma.next.app.services.project

import ru.isma.next.app.events.project.NewBlueprintProjectEvent
import ru.isma.next.app.events.project.NewProjectEvent
import ru.isma.next.app.models.projects.BlueprintProjectModel
import ru.isma.next.app.models.projects.IProjectModel
import ru.isma.next.app.models.projects.LismaProjectModel
import tornadofx.FX

class ProjectService {
    private val projects = mutableSetOf<IProjectModel>()

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
        FX.eventbus.fire(NewProjectEvent(project))
    }

    fun addBlueprint(project: BlueprintProjectModel){
        projects.add(project)
        FX.eventbus.fire(NewBlueprintProjectEvent(project))
    }

    fun close(project: IProjectModel){
        projects.remove(project)
    }

    fun getAllProjects() = projects.toTypedArray()
}