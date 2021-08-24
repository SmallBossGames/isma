package ru.isma.next.app.views.settings

import javafx.scene.control.ScrollPane
import ru.isma.next.app.extentions.bindDouble
import ru.isma.next.app.extentions.numberTextField
import ru.isma.next.app.services.simualtion.SimulationParametersService
import ru.isma.next.app.views.controls.PropertiesGrid
import tornadofx.*

class EventDetectionView(
    private val parametersService: SimulationParametersService
) : View("Event detection") {
    override val root =
        ScrollPane(
            PropertiesGrid().apply {
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