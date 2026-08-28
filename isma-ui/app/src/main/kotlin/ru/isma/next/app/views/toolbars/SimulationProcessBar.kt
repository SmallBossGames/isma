package ru.isma.next.app.views.toolbars

import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Separator
import javafx.scene.control.ToolBar
import javafx.scene.control.Tooltip
import org.controlsfx.control.PopOver
import ru.isma.next.app.extensions.matIconMZ
import ru.isma.next.app.viewmodels.MainCommandsViewModel

class SimulationProcessBar(
    private val commands: MainCommandsViewModel,
    private val tasksPopOver: TasksPopOver,
) : ToolBar() {

    init {
        tasksPopOver.apply {
            arrowLocation = PopOver.ArrowLocation.BOTTOM_LEFT
        }

        items.addAll(
            Button().apply {
                graphic = matIconMZ("play_arrow")
                tooltip = Tooltip("Play")
                onAction = EventHandler {
                    commands.simulate()
                }
            },
            Separator(),
            Button().apply {
                text = "Tasks"
                onAction = EventHandler {
                    if (tasksPopOver.isShowing) {
                        tasksPopOver.hide()
                    } else {
                        tasksPopOver.show(this)
                    }
                }
            }
        )
    }
}
