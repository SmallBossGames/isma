package controllers

import events.NewBlueprintProjectEvent
import events.NewProjectEvent
import models.IsmaProjectModel
import org.koin.core.component.KoinComponent
import tornadofx.Controller

class ProjectController : Controller(), KoinComponent {
    private val projects = mutableSetOf<IsmaProjectModel>()

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
        fire(NewProjectEvent(project))
        println(projects.count())
    }

    fun addBlueprint(project: IsmaProjectModel){
        projects.add(project)
        fire(NewBlueprintProjectEvent(project))
        println(projects.count())
    }

    fun close(project: IsmaProjectModel){
        projects.remove(project)
        println(projects.count())
    }

    fun getAllProjects() = projects.toTypedArray()
}