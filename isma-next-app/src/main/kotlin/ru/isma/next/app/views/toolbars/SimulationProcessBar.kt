package ru.isma.next.app.views.toolbars

import javafx.event.EventHandler
import javafx.scene.control.*
import javafx.scene.image.ImageView
import ru.isma.next.app.services.simualtion.SimulationResultService
import ru.isma.next.app.services.simualtion.SimulationService

class SimulationProcessBar(
    private val simulationResult: SimulationResultService,
    private val simulationService: SimulationService,
) : ToolBar() {
    init {
        items.addAll(
            Button().apply {
                graphic = ImageView("icons/toolbar/play.png")
                tooltip = Tooltip("Play")
                onAction = EventHandler { simulationService.simulate() }
                managedProperty().bind(!simulationService.isSimulationInProgressProperty())
                visibleProperty().bind(!simulationService.isSimulationInProgressProperty())
            },
            Button().apply {
                graphic = ImageView("icons/toolbar/abort.png")
                tooltip = Tooltip("Abort")
                onAction = EventHandler { simulationService.stopCurrentSimulation() }
                managedProperty().bind(simulationService.isSimulationInProgressProperty())
                visibleProperty().bind(simulationService.isSimulationInProgressProperty())
            },
            Separator().apply {
                managedProperty().bind(simulationService.isSimulationInProgressProperty())
                visibleProperty().bind(simulationService.isSimulationInProgressProperty())
            },
            Label("Progress: ").apply {
                managedProperty().bind(simulationService.isSimulationInProgressProperty())
                visibleProperty().bind(simulationService.isSimulationInProgressProperty())
            },
            ProgressBar().apply {
                progressProperty().bind(simulationService.progressProperty())
                managedProperty().bind(simulationService.isSimulationInProgressProperty())
                visibleProperty().bind(simulationService.isSimulationInProgressProperty())
            },
            Separator().apply {
                managedProperty().bind(simulationResult.isResultAvailableProperty())
                visibleProperty().bind(simulationResult.isResultAvailableProperty())
            },
            Label("Results: ").apply {
                managedProperty().bind(simulationResult.isResultAvailableProperty())
                visibleProperty().bind(simulationResult.isResultAvailableProperty())
            },
            Button("Show").apply {
                onAction = EventHandler { simulationResult.showChart() }
                managedProperty().bind(simulationResult.isResultAvailableProperty())
                visibleProperty().bind(simulationResult.isResultAvailableProperty())
            },
            Button("Export").apply {
                onAction = EventHandler { simulationResult.exportToFile() }
                managedProperty().bind(simulationResult.isResultAvailableProperty())
                visibleProperty().bind(simulationResult.isResultAvailableProperty())
            },
            Button("Clean").apply {
                onAction = EventHandler { simulationResult.clean() }
                managedProperty().bind(simulationResult.isResultAvailableProperty())
                visibleProperty().bind(simulationResult.isResultAvailableProperty())
            }
        )
    }
}