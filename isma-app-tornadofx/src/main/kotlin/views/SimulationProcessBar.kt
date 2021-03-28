package views

import controllers.SimulationController
import controllers.SimulationResultController
import javafx.scene.control.Tooltip
import javafx.scene.image.ImageView
import tornadofx.*

class SimulationProcessBar : View() {
    private val simulationResult by inject<SimulationResultController>()
    private val simulationController: SimulationController by inject()

    override val root = toolbar {
        button {
            graphic = ImageView("icons/toolbar/play.png")
            tooltip = Tooltip("Play")
            action { simulationController.simulate() }
            managedWhen(!simulationController.isSimulationInProgressProperty())
            hiddenWhen(simulationController.isSimulationInProgressProperty())
        }
        button {
            graphic = ImageView("icons/toolbar/abort.png")
            tooltip = Tooltip("Play")
            action { simulationController.simulate() }
            managedWhen(simulationController.isSimulationInProgressProperty())
            hiddenWhen(!simulationController.isSimulationInProgressProperty())
        }
        separator {
            managedWhen(simulationController.isSimulationInProgressProperty())
            hiddenWhen(!simulationController.isSimulationInProgressProperty())
        }
        label("Progress: ") {
            managedWhen(simulationController.isSimulationInProgressProperty())
            hiddenWhen(!simulationController.isSimulationInProgressProperty())
        }
        progressbar {
            progressProperty().bind(simulationController.progressProperty())
            managedWhen(simulationController.isSimulationInProgressProperty())
            hiddenWhen(!simulationController.isSimulationInProgressProperty())
        }
        separator {
            managedWhen(simulationResult.isResultAvailableProperty())
            hiddenWhen(!simulationResult.isResultAvailableProperty())
        }
        label("Results: "){
            managedWhen(simulationResult.isResultAvailableProperty())
            hiddenWhen(!simulationResult.isResultAvailableProperty())
        }
        button {
            text = "Show"
            action {
                simulationResult.showChart()
            }
            managedWhen(simulationResult.isResultAvailableProperty())
            hiddenWhen(!simulationResult.isResultAvailableProperty())
        }
        button {
            text = "Export"
            action {
                simulationResult.exportToFile()
            }
            managedWhen(simulationResult.isResultAvailableProperty())
            hiddenWhen(!simulationResult.isResultAvailableProperty())
        }
        button {
            text = "Clean"
            action {
                simulationResult.clean()
            }
            managedWhen(simulationResult.isResultAvailableProperty())
            hiddenWhen(!simulationResult.isResultAvailableProperty())
        }

    }
}