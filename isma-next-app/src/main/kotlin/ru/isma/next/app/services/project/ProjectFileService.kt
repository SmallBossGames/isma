package ru.isma.next.app.services.project

import javafx.stage.FileChooser
import javafx.stage.Window
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.isma.next.app.constants.OLD_ISMA_PROJECT_FILE
import ru.isma.next.app.constants.STATE_CHART_ISMA_PROJECT_FILE
import ru.isma.next.app.constants.TEXT_ISMA_PROJECT_FILE
import ru.isma.next.app.models.projects.BlueprintProjectModel
import ru.isma.next.app.models.projects.IProjectModel
import ru.isma.next.app.models.projects.LismaProjectModel
import java.io.File

class ProjectFileService(private val projectController: ProjectService) {
    fun listAllFilesPaths(): List<String> {
        return projectController.getAllProjects().mapNotNull { it.file?.path }
    }

    fun saveAs() {
        val project = projectController.activeProject ?: return
        saveProjectAs(project)
    }

    fun save() {
        val project = projectController.activeProject ?: return
        saveProject(project)
    }

    fun open(ownerWindow: Window? = null) {
        val file = FileChooser().run {
            title = "Open Project File"
            extensionFilters.addAll(allProjectFileFilters)
            return@run showOpenDialog(ownerWindow)
        } ?: return

        if(file.exists()){
            open(file)
        }
    }

    fun open(vararg paths: String) {
        paths.forEach {
            val file = File(it)

            if(file.exists()) {
                open(file)
            }
        }
    }

    fun open(file: File) {
        when {
            OLD_ISMA_PROJECT_FILE.contains(file.extension) -> {
                TODO("Fix backward compatibility")
            }
            TEXT_ISMA_PROJECT_FILE.contains(file.extension) -> {
                LismaProjectModel().apply {
                    this.name = file.name
                    this.file = file
                    this.lismaText = file.readText()
                    projectController.addText(this)
                }
            }
            STATE_CHART_ISMA_PROJECT_FILE.contains(file.extension) -> {
                BlueprintProjectModel().apply {
                    this.name = file.name
                    this.file = file
                    this.blueprint = Json.decodeFromString(file.readText())
                    projectController.addBlueprint(this)
                }
            }
        }
    }

    fun saveAll(){
        for (item in projectController.getAllProjects()){
            saveProject(item)
        }
    }

    private fun saveProject(project: IProjectModel){
        if(project.file == null){
            return saveProjectAs(project)
        }

        val fileOutput: String = when (project) {
            is LismaProjectModel -> {
                project.lismaText
            }
            is BlueprintProjectModel -> {
                Json.encodeToString(project.blueprint)
            }
            else -> {
                throw NotImplementedError()
            }
        }

        project.file!!.writeText(fileOutput)
    }

    private fun saveProjectAs(project: IProjectModel, ownerWindow: Window? = null){
        val filters: Array<FileChooser.ExtensionFilter>
        val fileOutput: String

        when (project) {
            is LismaProjectModel -> {
                filters = textProjectFileFilters
                fileOutput = project.lismaText
            }
            is BlueprintProjectModel -> {
                filters = stateChartProjectFileFilters
                fileOutput = Json.encodeToString(project.blueprint)
            }
            else -> {
                throw NotImplementedError()
            }
        }

        val file = FileChooser().run {
            title = "Save Project File"
            extensionFilters.addAll(filters)
            return@run showSaveDialog(ownerWindow)
        } ?: return

        project.apply {
            this.name = file.name
            this.file = file
        }

        file.writeText(fileOutput)
    }

    companion object {
        private val textProjectFileFilters = arrayOf(
            FileChooser.ExtensionFilter("ISMA Next Project file", TEXT_ISMA_PROJECT_FILE),
            FileChooser.ExtensionFilter("ISMA Project file", OLD_ISMA_PROJECT_FILE),
        )

        private val stateChartProjectFileFilters = arrayOf(
            FileChooser.ExtensionFilter("ISMA State Chart Project file", STATE_CHART_ISMA_PROJECT_FILE),
        )

        private val allProjectFileFilters = arrayOf(
            FileChooser.ExtensionFilter("All ISMA project files", TEXT_ISMA_PROJECT_FILE, STATE_CHART_ISMA_PROJECT_FILE),
        )
    }
}