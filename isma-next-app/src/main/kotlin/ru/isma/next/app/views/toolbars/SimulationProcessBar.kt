package ru.isma.next.app.views.toolbars

import javafx.scene.control.Tooltip
import javafx.scene.image.ImageView
import ru.isma.next.app.services.simualtion.SimulationResultService
import ru.isma.next.app.services.simualtion.SimulationService
import tornadofx.*

class SimulationProcessBar : View() {
    private val simulationResult: SimulationResultService by di()
    private val simulationService: SimulationService by di()

    override val root = toolbar {
        button {
            graphic = ImageView("icons/toolbar/play.png")
            tooltip = Tooltip("Play")
            action { simulationService.simulate() }
            managedWhen(!simulationService.isSimulationInProgressProperty())
            hiddenWhen(simulationService.isSimulationInProgressProperty())
        }
        button {
            graphic = ImageView("icons/toolbar/abort.png")
            tooltip = Tooltip("Abort")
            action { simulationService.stopCurrentSimulation() }
            managedWhen(simulationService.isSimulationInProgressProperty())
            hiddenWhen(!simulationService.isSimulationInProgressProperty())
        }
        separator {
            managedWhen(simulationService.isSimulationInProgressProperty())
            hiddenWhen(!simulationService.isSimulationInProgressProperty())
        }
        label("Progress: ") {
            managedWhen(simulationService.isSimulationInProgressProperty())
            hiddenWhen(!simulationService.isSimulationInProgressProperty())
        }
        progressbar {
            progressProperty().bind(simulationService.progressProperty())
            managedWhen(simulationService.isSimulationInProgressProperty())
            hiddenWhen(!simulationService.isSimulationInProgressProperty())
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