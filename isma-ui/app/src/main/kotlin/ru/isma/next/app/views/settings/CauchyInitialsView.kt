package ru.isma.next.app.views.settings

import javafx.scene.control.ScrollPane
import javafx.scene.layout.Pane
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.isma.next.app.viewmodels.SimulationParametersViewModel

class CauchyInitialsView(
    private val parametersViewModel: SimulationParametersViewModel
) : Pane() {
    val title: String = "Initials"

    init {
        val scrollPane = ScrollPane(
            propertiesGrid {
                addNode("Start", parametersViewModel.cauchyInitials.startTimeProperty)
                addNode("End", parametersViewModel.cauchyInitials.endTimeProperty)
                addNode("Step", parametersViewModel.cauchyInitials.stepProperty)
            }
        )
        scrollPane.isFitToWidth = true
        children.add(scrollPane)
    }
}
