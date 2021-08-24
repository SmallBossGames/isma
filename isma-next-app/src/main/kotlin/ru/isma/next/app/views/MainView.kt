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
    private val simulationProcess: SimulationProcessBar,
    private val ismaErrorListTable: IsmaErrorListTable,
    ismaMenuBar: IsmaMenuBar,
    ismaToolBar: IsmaToolBar,
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

        //left = Drawer()

        stylesheets.add("style.css")
    }
}