package controllers

import javafx.stage.FileChooser
import models.IsmaProjectModel
import tornadofx.Controller
import tornadofx.FileChooserMode
import tornadofx.chooseFile

class FileController : Controller() {
    private val activeProjectController: ActiveProjectController by inject()
    private val projectController: ProjectController by inject()
    private val fileFilers = arrayOf(
            FileChooser.ExtensionFilter("ISMA Next Project file", "*.im2"),
            FileChooser.ExtensionFilter("ISMA Project file", "*.im")
    )

    fun saveAs() {
        val project = activeProjectController.activeProject ?: return
        saveProjectAs(project)
    }

    fun save() {
        val project = activeProjectController.activeProject ?: return
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