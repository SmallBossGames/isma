package ru.isma.next.app.views.settings

import javafx.scene.control.ScrollPane
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.isma.next.app.services.simualtion.SimulationParametersService
import tornadofx.View


class CauchyInitialsView(
    private val parametersService: SimulationParametersService
): View("Initials") {
    override val root =
        ScrollPane(
            propertiesGrid {
                addNode("Start", parametersService.cauchyInitials.startTimeProperty())
                addNode("End", parametersService.cauchyInitials.endTimeProperty())
                addNode("Step", parametersService.cauchyInitials.stepProperty())
            }
        )
}
