package ru.isma.next.app.views.settings

import javafx.geometry.Insets
import javafx.scene.control.ScrollPane
import ru.isma.next.app.services.simualtion.SimulationParametersService
import ru.isma.next.app.views.controls.PropertiesGrid
import tornadofx.*


class CauchyInitialsView(
    private val parametersService: SimulationParametersService
): View("Initials") {
    override val root =
        ScrollPane(
            PropertiesGrid().apply {
                addNode("Start", parametersService.cauchyInitials.startTimeProperty())
                addNode("End", parametersService.cauchyInitials.endTimeProperty())
                addNode("Step", parametersService.cauchyInitials.stepProperty())
            }
        )
}
