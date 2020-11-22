package views

import controllers.SimulationProgressController
import javafx.application.Platform
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.scene.Parent
import tornadofx.*
import kotlin.concurrent.thread

class SimulationProcessView : View() {
    private val simulationProgressController by inject<SimulationProgressController>()

    override val root = vbox(10) {
        padding = Insets(10.0)
        hbox(5) {
            label("Execution state: ")
            label("waiting for start")
        }
        hbox(5) {
            label("Progress: ")
            progressbar {
                progressProperty().bind(simulationProgressController.progressProperty)
            }
        }
        hbox(5) {
            button {
                text = "Show chart"
            }
            button {
                text = "Export results"
            }
            separator{
                orientation = Orientation.VERTICAL
            }
            button {
                text = "Clean"
            }
        }

    }
}