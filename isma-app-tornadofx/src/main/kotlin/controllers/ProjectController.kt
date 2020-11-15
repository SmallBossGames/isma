package controllers

import events.NewProjectEvent
import models.IsmaProjectModel
import tornadofx.Controller

class ProjectController : Controller() {
    private val projects = mutableSetOf<IsmaProjectModel>()

    fun createNew(name: String){
        val project = IsmaProjectModel(name);
        projects.add(project)
        fire(NewProjectEvent(project))
        println(projects.count())
    }

    fun close(project: IsmaProjectModel){
        projects.remove(project)
        println(projects.count())
    }
}