import controllers.SyntaxErrorController
import javafx.geometry.Side
import javafx.scene.control.ToggleGroup
import models.SyntaxErrorModel
import tornadofx.*
import views.*
import views.simulation.settings.SettingsPanelView

class MainView : View() {
    private val ismaMenuBar: IsmaMenuBar by inject()
    private val ismaToolBar: IsmaToolBar by inject()
    private val ismaErrorListTable: IsmaErrorListTable by inject()
    private val ismaEditorTabPane: IsmaEditorTabPane by inject()
    private val simulationProcess: SimulationProcessView by inject()
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
            drawer {
                item("Error list") {
                    add(ismaErrorListTable)
                }
                item("Simulation") {
                    add(simulationProcess)
                }
            }
        }

        right {
            add(settingsPanel)
        }
    }
}