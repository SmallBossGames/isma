package controllers

import events.NewBlueprintProjectEvent
import models.IsmaProjectModel
import tornadofx.Controller

class ProjectController : Controller() {
    private val projects = mutableSetOf<IsmaProjectModel>()

    fun createNew(name: String){
        val project = IsmaProjectModel(name)
        add(project)
    }

    fun createNew(){
        val defaultProjectName = "New project"
        createNew(defaultProjectName)
    }

    fun add(project: IsmaProjectModel){
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