package ru.isma.next.app.views

import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import org.koin.core.component.KoinComponent
import ru.isma.next.app.models.preferences.DefaultFilesPreferencesModel
import ru.isma.next.app.models.preferences.WindowPreferencesModel
import ru.isma.next.app.services.preferences.PreferencesProvider
import ru.isma.next.app.services.project.ProjectFileService
import ru.isma.next.app.views.layout.Drawer
import ru.isma.next.app.views.settings.SettingsPanelView
import ru.isma.next.app.views.tabpane.IsmaEditorTabPane
import ru.isma.next.app.views.toolbars.IsmaErrorListTable
import ru.isma.next.app.views.toolbars.IsmaMenuBar
import ru.isma.next.app.views.toolbars.IsmaToolBar
import ru.isma.next.app.views.toolbars.SimulationProcessBar
import tornadofx.*

class MainView(
    private val preferencesProvider: PreferencesProvider,
    private val projectFileService: ProjectFileService,
    ismaMenuBar: IsmaMenuBar,
    ismaToolBar: IsmaToolBar,
    private val simulationProcess: SimulationProcessBar,
    private val ismaErrorListTable: IsmaErrorListTable,
    ismaEditorTabPane: IsmaEditorTabPane,
    settingsPanel: SettingsPanelView,
) : BorderPane() {
    init {
        top = VBox(
            ismaMenuBar,
            ismaToolBar,
        )

        center = ismaEditorTabPane

        bottom = BorderPane().apply {
            top = drawer {
                item("Error list") {
                    add(ismaErrorListTable)
                }
            }
            bottom = VBox(
                simulationProcess
            )
        }

        right = settingsPanel.root

        stylesheets.add("style.css")
    }

   /* override fun onDock() {
        super.onDock()

        val windowProps = preferencesProvider.preferences.windowPreferences
        val defaultFilesPreferences = preferencesProvider.preferences.defaultFilesPreferencesModel

        currentStage?.apply {
            isMaximized = windowProps.isMaximized
            height = windowProps.height
            width = windowProps.width
            x = windowProps.x
            y = windowProps.y

            minHeight = 500.0
            minWidth = 800.0

            projectFileService.open(*defaultFilesPreferences.lastOpenedProjectPath)
        }
    }

    override fun onUndock() {
        super.onUndock()

        currentStage?.also {
            val preferencesModel = WindowPreferencesModel(
                isMaximized = it.isMaximized,
                height = it.height,
                width = it.width,
                x = it.x,
                y = it.y,
            )

            val defaultFilesPreferences = DefaultFilesPreferencesModel(
                lastOpenedProjectPath = projectFileService.listAllFilesPaths().toTypedArray()
            )

            preferencesProvider.commit(preferencesModel)
            preferencesProvider.commit(defaultFilesPreferences)
        }
    }*/
}