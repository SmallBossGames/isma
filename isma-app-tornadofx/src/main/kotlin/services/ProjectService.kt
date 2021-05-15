package services

import events.NewBlueprintProjectEvent
import events.NewProjectEvent
import models.IsmaProjectModel
import tornadofx.FX

class ProjectService {
    private val projects = mutableSetOf<IsmaProjectModel>()

    public var activeProject: IsmaProjectModel? = null

    fun createNewBlueprint(name: String){
        val project = IsmaProjectModel(name)
        addBlueprint(project)
    }

    fun createNew(name: String){
        val project = IsmaProjectModel(name)
        addText(project)
    }

    fun createNew(){
        val defaultProjectName = "New project"
        createNew(defaultProjectName)
    }

    fun createNewBlueprint(){
        val defaultProjectName = "New blueprint"
        createNewBlueprint(defaultProjectName)
    }

    fun addText(project: IsmaProjectModel){
        projects.add(project)
        FX.eventbus.fire(NewProjectEvent(project))
        println(projects.count())
    }

    fun addBlueprint(project: IsmaProjectModel){
        projects.add(project)
        FX.eventbus.fire(NewBlueprintProjectEvent(project))
        println(projects.count())
    }

    fun close(project: IsmaProjectModel){
        projects.remove(project)
        println(projects.count())
    }

    fun getAllProjects() = projects.toTypedArray()
}