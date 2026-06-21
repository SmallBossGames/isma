package ru.isma.next.app.views.settings

import javafx.collections.FXCollections
import javafx.scene.control.ScrollPane
import javafx.scene.layout.Pane
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.isma.next.app.services.simualtion.SimulationParametersService
import ru.isma.next.app.models.simulation.SaveTarget

class ResultProcessingView(
    private val parametersService: SimulationParametersService
) : Pane() {
    val title: String = "Result processing"

    init {
        val scrollPane = ScrollPane(
            propertiesGrid {
                addNode(
                    "Save result",
                    FXCollections.observableArrayList(SaveTarget.values().toList()),
                    parametersService.resultSaving.savingTargetProperty
                )
            }
        )
        scrollPane.isFitToWidth = true
        children.add(scrollPane)
    }
}
