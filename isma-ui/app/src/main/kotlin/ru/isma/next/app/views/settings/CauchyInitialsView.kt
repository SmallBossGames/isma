package ru.isma.next.app.views.settings

import javafx.scene.control.ScrollPane
import javafx.scene.layout.Pane
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.isma.next.app.services.simulation.SimulationParametersService

class CauchyInitialsView(
    private val parametersService: SimulationParametersService
) : Pane() {
    val title: String = "Initials"

    init {
        val scrollPane = ScrollPane(
            propertiesGrid {
                addNode("Start", parametersService.cauchyInitials.startTimeProperty)
                addNode("End", parametersService.cauchyInitials.endTimeProperty)
                addNode("Step", parametersService.cauchyInitials.stepProperty)
            }
        )
        scrollPane.isFitToWidth = true
        children.add(scrollPane)
    }
}
