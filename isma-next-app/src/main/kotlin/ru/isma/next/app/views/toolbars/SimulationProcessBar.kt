package ru.isma.next.app.views.toolbars

import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Separator
import javafx.scene.control.ToolBar
import javafx.scene.control.Tooltip
import org.controlsfx.control.PopOver
import org.koin.core.component.KoinComponent
import ru.isma.next.app.extentions.matIconMZ
import ru.isma.next.app.services.simualtion.SimulationResultService
import ru.isma.next.app.services.simualtion.SimulationService

class SimulationProcessBar(
    simulationResultService: SimulationResultService,
    private val simulationService: SimulationService,
) : ToolBar(), KoinComponent {

    private val tasksPopOver = TasksPopOver(simulationResultService, simulationService).apply {
        arrowLocation = PopOver.ArrowLocation.BOTTOM_LEFT
    }

    init {
        items.addAll(
            Button().apply {
                graphic = matIconMZ("play_arrow")
                tooltip = Tooltip("Play")
                onAction = EventHandler {
                    simulationService.simulate()
                }
            },
            Separator(),
            Button().apply {
                text = "Tasks"
                onAction = EventHandler {
                    tasksPopOver.show(this)
                }
            }
        )
    }
}