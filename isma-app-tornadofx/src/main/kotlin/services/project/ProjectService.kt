package services.project

import events.project.NewBlueprintProjectEvent
import events.project.NewProjectEvent
import models.projects.BlueprintProjectModel
import models.projects.IProjectModel
import models.projects.LismaProjectModel
import tornadofx.FX

class ProjectService {
    private val projects = mutableSetOf<IProjectModel>()

    public var activeProject: IProjectModel? = null

    fun createNewBlueprint(name: String = "New blueprint") {
        val bpm = BlueprintProjectModel()
        bpm.name = name
        addBlueprint(bpm)

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
        println(projects.count())
    }

    fun addBlueprint(project: BlueprintProjectModel){
        projects.add(project)
        FX.eventbus.fire(NewBlueprintProjectEvent(project))
        println(projects.count())
    }

    fun close(project: IProjectModel){
        projects.remove(project)
        println(projects.count())
    }

    fun getAllProjects() = projects.toTypedArray()
}