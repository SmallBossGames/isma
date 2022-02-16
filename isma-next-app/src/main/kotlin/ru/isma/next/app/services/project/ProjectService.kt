package ru.isma.next.app.services.project

import javafx.collections.FXCollections
import javafx.collections.SetChangeListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import ru.isma.next.app.models.projects.BlueprintProjectModel
import ru.isma.next.app.models.projects.IProjectModel
import ru.isma.next.app.models.projects.LismaProjectModel

class ProjectService {
    private val projects = FXCollections.observableSet<IProjectModel>()

    val existedProjects = projects.asFlow()

    val newProjects = callbackFlow {
        val listener = SetChangeListener<IProjectModel?> {
            val element = it.elementAdded ?: return@SetChangeListener

            coroutineScope.launch {
                send(element)
            }
        }

        projects.addListener(listener)

        awaitClose {
            projects.removeListener(listener)
        }
    }

    val removedProjects = callbackFlow {
        val listener = SetChangeListener<IProjectModel?> {
            val element = it.elementRemoved ?: return@SetChangeListener

            coroutineScope.launch {
                send(element)
            }
        }

        projects.addListener(listener)

        awaitClose {
            projects.removeListener(listener)
        }
    }

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