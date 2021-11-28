package ru.isma.next.app.views.toolbars

import javafx.event.EventHandler
import javafx.scene.control.*
import org.controlsfx.control.PopOver
import ru.isma.next.app.extentions.matIconAL
import ru.isma.next.app.extentions.matIconMZ
import ru.isma.next.app.services.simualtion.SimulationResultService
import ru.isma.next.app.services.simualtion.SimulationService

class SimulationProcessBar(
    private val simulationResultService: SimulationResultService,
    private val simulationService: SimulationService,
) : ToolBar() {

    private val popover = TasksPopOver(simulationResultService).apply {
        arrowLocation = PopOver.ArrowLocation.BOTTOM_LEFT
        bindInProgressTasksList(simulationService.trackingTasks)
        bindCompletedSimulationModel(simulationResultService.trackingTasksResults)
    }

    init {
        items.addAll(
            Button().apply {
                graphic = matIconMZ("play_arrow")
                tooltip = Tooltip("Play")
                onAction = EventHandler { simulationService.simulate() }
                managedProperty().bind(!simulationService.isSimulationInProgressProperty())
                visibleProperty().bind(!simulationService.isSimulationInProgressProperty())
            },
            Separator(),
            Button().apply {
                text = "Tasks"
                onAction = EventHandler {
                    popover.show(this)
                }
            },
            Button().apply {
                graphic = matIconAL("close")
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
        )
    }
}