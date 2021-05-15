package services

import javafx.stage.FileChooser
import models.IsmaProjectModel
import org.koin.core.component.KoinComponent
import tornadofx.FileChooserMode
import tornadofx.chooseFile
import org.koin.core.component.inject as koinInject

class FileService(private val projectController: ProjectService) {
    private val fileFilers = arrayOf(
            FileChooser.ExtensionFilter("ISMA Next Project file", "*.im2"),
            FileChooser.ExtensionFilter("ISMA Project file", "*.im")
    )

    fun saveAs() {
        val project = projectController.activeProject ?: return
        saveProjectAs(project)
    }

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

        projectController.addText(IsmaProjectModel(file))
    }

    fun saveAll(){
        for (item in projectController.getAllProjects()){
            saveProject(item)
        }
    }

    private fun saveProject(project: IsmaProjectModel){
        if(project.file == null){
            return saveProjectAs(project)
        }

        project.file!!.writeText(project.projectText)
    }

    private fun saveProjectAs(project: IsmaProjectModel){
        val selectedFiles = chooseFile (filters = fileFilers, mode = FileChooserMode.Save)

        if(selectedFiles.isEmpty())
        {
            return
        }

        val file = selectedFiles.first()

        println(file.path)

        project.name = file.name
        project.file = file
        file.writeText(project.projectText)
    }
}