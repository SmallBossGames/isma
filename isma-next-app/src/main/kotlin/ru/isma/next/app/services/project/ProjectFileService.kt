package ru.isma.next.app.services.project

import ru.isma.next.app.constants.OLD_ISMA_PROJECT_FILE
import ru.isma.next.app.constants.STATE_CHART_ISMA_PROJECT_FILE
import ru.isma.next.app.constants.TEXT_ISMA_PROJECT_FILE
import javafx.stage.FileChooser
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.isma.next.app.models.projects.BlueprintProjectModel
import ru.isma.next.app.models.projects.IProjectModel
import ru.isma.next.app.models.projects.LismaProjectModel
import tornadofx.FileChooserMode
import tornadofx.chooseFile

class ProjectFileService(private val projectController: ProjectService) {
    private val textProjectFileFilters = arrayOf(
        FileChooser.ExtensionFilter("ISMA Project file", OLD_ISMA_PROJECT_FILE),
        FileChooser.ExtensionFilter("ISMA Next Project file", TEXT_ISMA_PROJECT_FILE),
    )

    private val stateChartProjectFileFilters = arrayOf(
        FileChooser.ExtensionFilter("ISMA State Chart Project file", STATE_CHART_ISMA_PROJECT_FILE),
    )

    fun saveAs() {
        val project = projectController.activeProject ?: return
        saveProjectAs(project)
    }

    private val fileFilers = arrayOf(*textProjectFileFilters, *stateChartProjectFileFilters)

    fun save() {
        val project = projectController.activeProject ?: return
        saveProject(project)
    }

    fun open() {
        val selectedFiles = chooseFile (filters = fileFilers, mode = FileChooserMode.Single)

        if(selectedFiles.isEmpty())
        {
            return
        }

        val file = selectedFiles.first()

        when {
            OLD_ISMA_PROJECT_FILE.contains(file.extension) -> {
                TODO("Fix backward compatibility")
            }
            TEXT_ISMA_PROJECT_FILE.contains(file.extension) -> {
                LismaProjectModel().apply {
                    this.name = file.name
                    this.lismaText = file.readText()
                    this.file = file
                    projectController.addText(this)
                }
            }
            STATE_CHART_ISMA_PROJECT_FILE.contains(file.extension) -> {
                val blueprint = BlueprintProjectModel().apply {
                    this.name = file.name
                    this.file = file
                    this.blueprint = Json.decodeFromString(file.readText())
                }
                projectController.addBlueprint(blueprint)
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

    private fun saveProjectAs(project: IProjectModel){
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

        val selectedFiles = chooseFile (filters = filters, mode = FileChooserMode.Save)

        if(selectedFiles.isEmpty())
        {
            return
        }

        val file = selectedFiles.first()

        project.apply {
            this.name = file.name
            this.file = file
        }

        file.writeText(fileOutput)
    }
}