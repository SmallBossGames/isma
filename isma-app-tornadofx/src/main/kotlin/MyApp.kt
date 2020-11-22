import controllers.SyntaxErrorController
import javafx.scene.control.ToggleGroup
import models.SyntaxErrorModel
import tornadofx.*
import views.*

class MyView : View() {

    private val syntaxErrorController: SyntaxErrorController by inject()
    private val ismaMenuBar: IsmaMenuBar by inject()
    private val ismaToolBar: IsmaToolBar by inject()
    private val ismaErrorListTable: IsmaErrorListTable by inject()
    private val ismaEditorTabPane: IsmaEditorTabPane by inject()
    private val simulationProcess: SimulationProcessView by inject()
    private val simulationSettingView: SimulationSettingsView by inject()

    private val toggleGroup = ToggleGroup()

    init {
        title = "ISMA Next"
    }

    override val root = borderpane {
        minHeight = 480.0
        minWidth = 640.0

        top = vbox {
            add(ismaMenuBar)
            add(ismaToolBar)
        }

        center {
            add(ismaEditorTabPane)
        }

        bottom {
            tabpane {
                tab("Error list") {
                    add(ismaErrorListTable)
                    isClosable = false
                }
                tab("Simulation") {
                    add(simulationProcess)
                    isClosable = false
                }
            }

        }

        right{
            add(simulationSettingView)
        }
    }
}