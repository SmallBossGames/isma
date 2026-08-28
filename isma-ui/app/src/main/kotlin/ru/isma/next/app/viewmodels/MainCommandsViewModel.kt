package ru.isma.next.app.viewmodels

import javafx.application.Platform
import javafx.stage.FileChooser
import javafx.stage.Window
import ru.isma.next.app.constants.OLD_ISMA_PROJECT_FILE
import ru.isma.next.app.constants.STATE_CHART_ISMA_PROJECT_FILE
import ru.isma.next.app.constants.TEXT_ISMA_PROJECT_FILE
import ru.isma.next.app.models.projects.BlueprintProjectModel
import ru.isma.next.app.models.projects.IProjectModel
import ru.isma.next.app.models.projects.LismaProjectModel
import ru.isma.next.app.services.project.IProjectService
import ru.isma.next.app.services.project.LismaPdeService
import ru.isma.next.app.services.project.ProjectEditorPort
import ru.isma.next.app.services.project.ProjectFileService
import ru.isma.next.app.services.simulation.ISimulationService
import ru.isma.next.editor.text.services.contracts.IEditorPlatformService

class MainCommandsViewModel(
    private val projectService: IProjectService,
    private val projectFileService: ProjectFileService,
    private val editorPort: ProjectEditorPort,
    private val lismaPdeService: LismaPdeService,
    private val editorPlatformService: IEditorPlatformService,
    private val simulationService: ISimulationService,
    private val parametersViewModel: SimulationParametersViewModel,
    private val editorTabPaneViewModel: EditorTabPaneViewModel,
) {
    fun newProject() {
        projectService.createNew()
    }

    fun newBlueprint() {
        projectService.createNewBlueprint()
    }

    fun open(ownerWindow: Window? = null) {
        val file = FileChooser().run {
            title = "Open Project File"
            extensionFilters.addAll(allProjectFileFilters)
            return@run showOpenDialog(ownerWindow)
        } ?: return

        if (file.exists()) {
            projectFileService.open(file)
        }
    }

    fun save() {
        val project = activeProject ?: return

        if (project.file == null) {
            saveAs()
        } else {
            projectFileService.save(project)
        }
    }

    fun saveAs(ownerWindow: Window? = null) {
        val project = activeProject ?: return
        saveAsProject(project, ownerWindow)
    }

    fun saveAll(ownerWindow: Window? = null) {
        projectService.projects.forEach { project ->
            if (project.file == null) {
                saveAsProject(project, ownerWindow)
            } else {
                projectFileService.save(project)
            }
        }
    }

    fun closeActive() {
        val project = editorTabPaneViewModel.activeProject ?: return
        editorTabPaneViewModel.closeProject(project)
    }

    fun closeAll() {
        editorTabPaneViewModel.closeAllProjects()
    }

    fun exit() {
        Platform.exit()
    }

    fun cut() {
        editorPlatformService.cut()
    }

    fun copy() {
        editorPlatformService.copy()
    }

    fun paste() {
        editorPlatformService.paste()
    }

    fun verify() {
        val project = activeProject ?: return
        lismaPdeService.translateLisma(editorPort.content(project).lisma)
    }

    fun simulate() {
        val project = activeProject ?: return
        simulationService.simulate(project, parametersViewModel.snapshot())
    }

    fun storeSettings(ownerWindow: Window? = null) {
        parametersViewModel.store(ownerWindow)
    }

    fun loadSettings(ownerWindow: Window? = null) {
        parametersViewModel.load(ownerWindow)
    }

    private val activeProject: IProjectModel?
        get() = editorTabPaneViewModel.activeProject?.model

    private fun saveAsProject(project: IProjectModel, ownerWindow: Window?) {
        val file = FileChooser().run {
            title = "Save Project File"
            extensionFilters.addAll(fileFiltersFor(project))
            return@run showSaveDialog(ownerWindow)
        } ?: return

        projectFileService.saveAs(project, file)
        editorTabPaneViewModel.refreshName(project)
    }

    private fun fileFiltersFor(project: IProjectModel) = when (project) {
        is LismaProjectModel -> textProjectFileFilters
        is BlueprintProjectModel -> stateChartProjectFileFilters
        else -> textProjectFileFilters
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
