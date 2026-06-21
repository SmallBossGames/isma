package ru.isma.next.app.views.settings

import javafx.scene.control.ScrollPane
import javafx.scene.layout.Pane
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.isma.next.app.services.simulation.SimulationParametersService

class EventDetectionView(
    private val parametersService: SimulationParametersService
) : Pane() {
    val title: String = "Event detection"

    init {
        val scrollPane = ScrollPane(
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
        scrollPane.isFitToWidth = true
        children.add(scrollPane)
    }
}
