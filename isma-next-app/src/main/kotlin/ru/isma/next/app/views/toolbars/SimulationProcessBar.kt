package ru.isma.next.app.views.toolbars

import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Separator
import javafx.scene.control.ToolBar
import javafx.scene.control.Tooltip
import org.controlsfx.control.PopOver
import org.koin.core.component.KoinComponent
import ru.isma.next.app.extentions.matIconMZ
import ru.isma.next.app.services.simualtion.SimulationService

class SimulationProcessBar(
    private val simulationService: SimulationService,
    private val tasksPopOver: TasksPopOver,
) : ToolBar(), KoinComponent {

    init {
        tasksPopOver.apply {
            arrowLocation = PopOver.ArrowLocation.BOTTOM_LEFT
        }

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