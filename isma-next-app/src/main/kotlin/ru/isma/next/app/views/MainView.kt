package ru.isma.next.app.views

import tornadofx.*
import ru.isma.next.app.views.toolbars.IsmaErrorListTable
import ru.isma.next.app.views.toolbars.IsmaMenuBar
import ru.isma.next.app.views.toolbars.IsmaToolBar
import ru.isma.next.app.views.toolbars.SimulationProcessBar
import ru.isma.next.app.views.tabpane.IsmaEditorTabPane
import ru.isma.next.app.views.settings.SettingsPanelView

class MainView : View() {
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
        minHeight = 480.0
        minWidth = 640.0

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
}