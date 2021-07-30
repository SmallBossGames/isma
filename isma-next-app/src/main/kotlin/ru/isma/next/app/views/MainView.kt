package ru.isma.next.app.views

import ru.isma.next.app.models.preferences.WindowPreferencesModel
import ru.isma.next.app.services.preferences.PreferencesProvider
import tornadofx.*
import ru.isma.next.app.views.toolbars.IsmaErrorListTable
import ru.isma.next.app.views.toolbars.IsmaMenuBar
import ru.isma.next.app.views.toolbars.IsmaToolBar
import ru.isma.next.app.views.toolbars.SimulationProcessBar
import ru.isma.next.app.views.tabpane.IsmaEditorTabPane
import ru.isma.next.app.views.settings.SettingsPanelView

class MainView : View() {
    private val preferencesProvider: PreferencesProvider by di()

    private val ismaMenuBar: IsmaMenuBar by inject()
    private val ismaToolBar: IsmaToolBar by inject()
    private val simulationProcess: SimulationProcessBar by inject()
    private val ismaErrorListTable: IsmaErrorListTable by inject()
    private val ismaEditorTabPane: IsmaEditorTabPane by inject()
    private val settingsPanel: SettingsPanelView by inject()

    init {
        title = "ISMA Next"
    }

    override val root = borderpane {
        top {
            vbox {
                add(ismaMenuBar)
                add(ismaToolBar)
            }
        }

        center {
            add(ismaEditorTabPane)
        }

        bottom {
            borderpane {
                top = drawer {
                    item("Error list") {
                        add(ismaErrorListTable)
                    }
                }
                bottom = vbox {
                    add(simulationProcess)
                }
            }
        }

        right {
            add(settingsPanel)
        }
    }

    override fun onDock() {
        super.onDock()

        val windowProps = preferencesProvider.preferences.windowPreferences

        currentStage?.apply {
            isMaximized = windowProps.isMaximized
            height = windowProps.height
            width = windowProps.width
            x = windowProps.x
            y = windowProps.y

            minHeight = 500.0
            minWidth = 800.0
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

            preferencesProvider.commit(preferencesModel)
        }
    }
}