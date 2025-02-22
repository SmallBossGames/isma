package ru.isma.next.app.views.settings

import javafx.scene.control.ScrollPane
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.isma.next.app.services.simualtion.SimulationParametersService
import tornadofx.View

class EventDetectionView(
    private val parametersService: SimulationParametersService
) : View("Event detection") {
    override val root =
        ScrollPane(
            propertiesGrid {
                addNode("In use", parametersService.eventDetection.isEventDetectionInUseProperty)
                addNode("Gamma", parametersService.eventDetection.gammaProperty).apply {
                    disableProperty().bind(parametersService.eventDetection.isEventDetectionInUseProperty.not())
                }
                addNode("Step limit", parametersService.eventDetection.isStepLimitInUseProperty)
                addNode("Low border", parametersService.eventDetection.lowBorderProperty).apply {
                    disableProperty().bind(parametersService.eventDetection.isStepLimitInUseProperty.not())
                }
            }
        )
}