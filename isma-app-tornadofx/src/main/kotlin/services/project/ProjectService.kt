package services.project

import events.NewBlueprintProjectEvent
import events.NewProjectEvent
import models.projects.BlueprintProjectModel
import models.projects.IProjectModel
import models.projects.LismaProjectModel
import tornadofx.FX

class ProjectService {
    private val projects = mutableSetOf<IProjectModel>()

    public var activeProject: IProjectModel? = null

    fun createNewBlueprint(name: String) {
        val bpm = BlueprintProjectModel()
        bpm.name = name
        addBlueprint(bpm)

    }

    fun createNew(name: String){
        LismaProjectModel().apply {
            this.name = name
            addText(this)
        }
    }

    fun createNew(){
        val defaultProjectName = "New project"
        createNew(defaultProjectName)
    }

    fun createNewBlueprint(){
        val defaultProjectName = "New blueprint"
        createNewBlueprint(defaultProjectName)
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