package ru.isma.next.app.views.settings

import javafx.scene.control.ScrollPane
import javafx.scene.layout.Pane
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.isma.next.app.viewmodels.SimulationParametersViewModel

class EventDetectionView(
    private val parametersViewModel: SimulationParametersViewModel
) : Pane() {
    val title: String = "Event detection"

    init {
        val eventDetection = parametersViewModel.eventDetection
        val scrollPane = ScrollPane(
            propertiesGrid {
                addNode("In use", eventDetection.isEventDetectionInUseProperty)
                addNode("Gamma", eventDetection.gammaProperty).apply {
                    disableProperty().bind(eventDetection.isEventDetectionInUseProperty.not())
                }
                addNode("Step limit", eventDetection.isStepLimitInUseProperty)
                addNode("Low border", eventDetection.lowBorderProperty).apply {
                    disableProperty().bind(eventDetection.isStepLimitInUseProperty.not())
                }
            }
        )
        scrollPane.isFitToWidth = true
        children.add(scrollPane)
    }
}
