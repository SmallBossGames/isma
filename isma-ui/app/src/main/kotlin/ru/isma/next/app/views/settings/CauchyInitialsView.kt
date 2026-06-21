package ru.isma.next.app.views.settings

import javafx.scene.control.ScrollPane
import javafx.scene.layout.Pane
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.isma.next.app.services.simualtion.SimulationParametersService

class CauchyInitialsView(
    private val parametersService: SimulationParametersService
) : Pane() {
    val title: String = "Initials"

    init {
        content = ScrollPane(
            propertiesGrid {
                addNode("Start", parametersService.cauchyInitials.startTimeProperty())
                addNode("End", parametersService.cauchyInitials.endTimeProperty())
                addNode("Step", parametersService.cauchyInitials.stepProperty())
            }
        )
    }
}
