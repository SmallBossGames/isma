package ru.isma.next.app.views

import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import ru.isma.next.app.views.layout.ErrorListDrawer
import ru.isma.next.app.views.settings.SettingsPanelView
import ru.isma.next.app.views.tabpane.IsmaEditorTabPane
import ru.isma.next.app.views.toolbars.IsmaMenuBar
import ru.isma.next.app.views.toolbars.IsmaToolBar
import ru.isma.next.app.views.toolbars.SimulationProcessBar

class MainView(
    private val simulationProcess: SimulationProcessBar,
    ismaErrorListDrawer: ErrorListDrawer,
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
            top = ismaErrorListDrawer
            bottom = VBox(
                simulationProcess
            )
        }

        right = settingsPanel

        //left = Drawer()

        stylesheets.add("style.css")
    }
}
